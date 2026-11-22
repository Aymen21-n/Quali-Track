package com.qualitrack.production.feignClient;

import com.qualitrack.production.exception.ServiceExterneIndisponibleException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class ResilientRequests {
    private static final Logger logger = LoggerFactory.getLogger(ResilientRequests.class);

    private final AtelierClient atelierClient;
    private final RawMaterialClient rawMaterialClient;
    private final UtilisateursClient utilisateursClient;
    private final CacheManager cacheManager;

    public ResilientRequests(AtelierClient atelierClient,
                             RawMaterialClient rawMaterialClient,
                             UtilisateursClient utilisateursClient,
                             CacheManager cacheManager) {
        this.atelierClient = atelierClient;
        this.rawMaterialClient = rawMaterialClient;
        this.utilisateursClient = utilisateursClient;
        this.cacheManager = cacheManager;
    }

    @Retry(name = "atelier-service", fallbackMethod = "getMachineByIdFbFallback")
    @CircuitBreaker(name = "atelier-service")
    public MachineDto getMachineByIdFb(Long id) {
        MachineDto result = atelierClient.getMachineById(id);
        put("machines", id, result);
        return result;
    }

    public MachineDto getMachineByIdFbFallback(Long id, Throwable throwable) {
        if (!estIndisponibleRuntime(throwable)) {
            throwAsRuntime(throwable);
        }
        logger.warn("Machine {} indisponible, utilisation du repli tolerant", id, throwable);
        Cache cache = cacheManager.getCache("machines");
        MachineDto cached = cache == null ? null : cache.get(id, MachineDto.class);
        if (cached != null) {
            return cached;
        }
        MachineDto dto = new MachineDto();
        dto.setId(id);
        return dto;
    }

    @Retry(name = "atelier-service", fallbackMethod = "getMachineByIdStrictFallback")
    @CircuitBreaker(name = "atelier-service")
    public MachineDto getMachineByIdStrict(Long id) {
        MachineDto result = atelierClient.getMachineById(id);
        put("machines", id, result);
        return result;
    }

    public MachineDto getMachineByIdStrictFallback(Long id, Throwable throwable) {
        if (estIndisponibleRuntime(throwable)) {
            throw new ServiceExterneIndisponibleException("Le service atelier est indisponible pour la machine " + id);
        }
        throwAsRuntime(throwable);
        return null;
    }

    @Retry(name = "atelier-service", fallbackMethod = "getRawMaterialByIdFbFallback")
    @CircuitBreaker(name = "atelier-service")
    public RawMaterialDto getRawMaterialByIdFb(Long id) {
        RawMaterialDto result = rawMaterialClient.getRawMaterialById(id);
        put("rawMaterials", id, result);
        return result;
    }

    public RawMaterialDto getRawMaterialByIdFbFallback(Long id, Throwable throwable) {
        if (!estIndisponibleRuntime(throwable)) {
            throwAsRuntime(throwable);
        }
        logger.warn("Matiere {} indisponible, utilisation du repli tolerant", id, throwable);
        Cache cache = cacheManager.getCache("rawMaterials");
        RawMaterialDto cached = cache == null ? null : cache.get(id, RawMaterialDto.class);
        if (cached != null) {
            return cached;
        }
        RawMaterialDto dto = new RawMaterialDto();
        dto.setId(id);
        return dto;
    }

    @Retry(name = "atelier-service", fallbackMethod = "getRawMaterialByIdStrictFallback")
    @CircuitBreaker(name = "atelier-service")
    public RawMaterialDto getRawMaterialByIdStrict(Long id) {
        RawMaterialDto result = rawMaterialClient.getRawMaterialById(id);
        put("rawMaterials", id, result);
        return result;
    }

    public RawMaterialDto getRawMaterialByIdStrictFallback(Long id, Throwable throwable) {
        if (estIndisponibleRuntime(throwable)) {
            throw new ServiceExterneIndisponibleException("Le service atelier est indisponible pour la matière première " + id);
        }
        throwAsRuntime(throwable);
        return null;
    }

    @Retry(name = "utilisateurs-service", fallbackMethod = "getUtilisateurByIdFbFallback")
    @CircuitBreaker(name = "utilisateurs-service")
    public UserDto getUtilisateurByIdFb(Long id) {
        UserDto result = utilisateursClient.getUserById(id);
        put("utilisateurs", id, result);
        return result;
    }

    public UserDto getUtilisateurByIdFbFallback(Long id, Throwable throwable) {
        if (!estIndisponibleRuntime(throwable)) {
            throwAsRuntime(throwable);
        }
        logger.warn("Utilisateur {} indisponible, utilisation du repli tolerant", id, throwable);
        Cache cache = cacheManager.getCache("utilisateurs");
        UserDto cached = cache == null ? null : cache.get(id, UserDto.class);
        if (cached != null) {
            return cached;
        }
        UserDto dto = new UserDto();
        dto.setId(id);
        return dto;
    }

    @Retry(name = "utilisateurs-service", fallbackMethod = "getUtilisateurByIdStrictFallback")
    @CircuitBreaker(name = "utilisateurs-service")
    public UserDto getUtilisateurByIdStrict(Long id) {
        UserDto result = utilisateursClient.getUserById(id);
        put("utilisateurs", id, result);
        return result;
    }

    public UserDto getUtilisateurByIdStrictFallback(Long id, Throwable throwable) {
        if (estIndisponibleRuntime(throwable)) {
            throw new ServiceExterneIndisponibleException("Le service utilisateurs est indisponible pour l'utilisateur " + id);
        }
        throwAsRuntime(throwable);
        return null;
    }

    public boolean estIndisponible(RuntimeException exception) {
        if (exception instanceof CallNotPermittedException) {
            return true;
        }
        if (exception instanceof FeignException feignException) {
            return feignException.status() == -1 || feignException.status() == 503;
        }
        return false;
    }

    private boolean estIndisponibleRuntime(Throwable throwable) {
        return throwable instanceof RuntimeException runtimeException && estIndisponible(runtimeException);
    }

    private void throwAsRuntime(Throwable throwable) {
        if (throwable instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        throw new RuntimeException(throwable);
    }

    private void put(String cacheName, Long id, Object value) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null && value != null) {
            cache.put(id, value);
        }
    }
}


package com.johnbryce.coupon_system_part_2.utils;


import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.exceptions.generic.GenericException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Utils {

    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    //Get only entity as parameter - every entity implementing EntityBase - an empty interface
    // Conversion from entity object to dto
    public <D> D convertToDto(EntityBase entity, Class<D> dto) throws CouponSystemException {
        try {
            return modelMapper.map(entity, dto);
        } catch (Exception e) {
            throw new CouponSystemException(GenericException.UNKNOWN_OBJECT_TYPE);
        }

    }

    //Get any object as parameter - and only convert the object to entity object if it's instance of
    //dto classes - Conversion from dto to object
    public <E, D> E convertFromDto(D dto, Class<E> entity) throws CouponSystemException {
        try {
            return modelMapper.map(dto, entity);
        } catch (Exception e) {
            throw new CouponSystemException(GenericException.UNKNOWN_OBJECT_TYPE);
        }
    }

    //From dtoList to objectList
    public <D, E> List<E> convertFromDtoToEntityList(List<D> dto, Class<E> entity) throws CouponSystemException {
        if (dto.isEmpty()) {
            throw new CouponSystemException(GenericException.EMPTY_LIST);
        }
        try {
            return dto.stream().map(d -> modelMapper.map(d, entity)).toList();
        } catch (Exception e) {
            throw new CouponSystemException(GenericException.UNKNOWN_OBJECT_TYPE);
        }

    }

    //From objectList to dtoList
    public <E, D> List<D> convertFromEntityListToDto(List<E> entity, Class<D> dto) throws CouponSystemException {
        if (entity.isEmpty()) {
            throw new CouponSystemException(GenericException.EMPTY_LIST);
        }
        try {
            return entity.stream().map(d -> modelMapper.map(d, dto)).toList();
        } catch (Exception e) {
            throw new CouponSystemException(GenericException.UNKNOWN_OBJECT_TYPE);
        }
    }

    public <T> T post
            (Class<T> responseHttp, String urlString, String addToUrl, T objectToPost) {
        HttpEntity<T> httpEntity = new HttpEntity<>(objectToPost);
        return restTemplate.exchange(urlString + addToUrl,
                HttpMethod.POST, httpEntity, responseHttp).getBody();
    }

    public Message postWithMessageResponse
            (String urlString, String addToUrl, Object objectToPost) {
        return restTemplate.postForEntity(
                urlString + addToUrl,
                objectToPost,
                Message.class
        ).getBody();
    }

    public Message postWithMessageResponseAndParams
            (String urlString, String addToUrl, Object objectToPost, Object... paramsToUrl) {
        return
                restTemplate.postForEntity(urlString + addToUrl,
                        objectToPost, Message.class, paramsToUrl).getBody();
    }

    public Message postWithMessageResponseAndParamsOnly
            (String urlString, String addToUrl, Object... paramsToUrl) {
        return
                restTemplate.postForEntity(urlString + addToUrl,
                        null, Message.class, paramsToUrl).getBody();
    }

    public <T> Message postWithMessageResponseAndPaths
            (String urlString, String addToUrl, T objectToPost,
             Object... pathVariables) {
        HttpEntity<T> post = new HttpEntity<>(objectToPost);
        return restTemplate.exchange(urlString + addToUrl,
                HttpMethod.POST, post, Message.class, pathVariables).getBody();
    }

    public <T> T updateWithPath
            (Class<T> responseHttp, String urlString, String addToUrl, T objectToUpdate, Object... Path) {
        HttpEntity<T> httpEntity = new HttpEntity<>(objectToUpdate);
        return restTemplate.exchange(urlString + addToUrl,
                HttpMethod.PUT, httpEntity, responseHttp, Path).getBody();
    }

    public <T, R> R updateWithDifferentReturnType
            (Class<R> responseHttp, String urlString, String addToUrl, T objectToUpdate,
             Object... pathVariables) {
        HttpEntity<T> update = new HttpEntity<>(objectToUpdate);
        return restTemplate.exchange(urlString + addToUrl,
                HttpMethod.PUT, update, responseHttp, pathVariables).getBody();
    }

    public <T> T get(String urlString, String addToUrl, Class<T> returnType) {
        return restTemplate.getForObject(urlString + addToUrl,
                returnType);
    }


    public <T> T getWithPath(String urlString, String addToUrl, Class<T>
            returnType, Object... paths) {
        return restTemplate.exchange(urlString + addToUrl,
                HttpMethod.GET, null, returnType, paths).getBody();
    }

    public <T> List<T> getListWithPath(String urlString, String addToUrl,
                                       Class<T[]> type, Object... paths) {
        T[] anyList = restTemplate.getForObject(urlString + addToUrl,
                type, paths);
        return anyList != null ? Arrays.asList(anyList) : Collections.emptyList();
    }

    public <T, R> List<T> getListWithRequestBody(String urlString, String addToUrl,
                                                 Class<T[]> type, R requestBody, Object... paths) {
        HttpEntity<R> httpsObject = new HttpEntity<>(requestBody);
        T[] anyList = restTemplate.exchange(urlString + addToUrl, HttpMethod.GET
                , httpsObject, type, paths).getBody();
        return anyList != null ? Arrays.asList(anyList) : Collections.emptyList();
    }

    public <T> T delete
            (Class<T> responseHttp, String urlString, String addToUrl, Object... Path) {
        return restTemplate.exchange(urlString + addToUrl,
                HttpMethod.DELETE, null, responseHttp, Path).getBody();
    }

}
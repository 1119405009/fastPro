package com.hzqykeji.banner.utils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by teruo on 2018/12/29.
 * 提取器.
 */
public class Extractor {
    private Extractor() {
    }
    public static String[] extractErrorMsg(BindingResult result) {
        List<ObjectError> errors = result.getAllErrors();
        String[] errorMsg = new String[errors.size()];
        for (int i = 0, len = errors.size(); i < len; i++) {
            errorMsg[i] = errors.get(i).getDefaultMessage();
        }
        return errorMsg;
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}

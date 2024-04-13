package com.group25.ecommercefashionapp.utilities;

import com.group25.ecommercefashionapp.data.ProductSize;

import java.util.*;

public class SizeUtils {
    private static final String[] SIZE_ORDER = {"XXXS","XXS", "XS", "S", "M", "L", "XL", "XXL","XXXL"};

    public static List<ProductSize> sortSizes(List<ProductSize> sizes) {
        List<ProductSize> sortedSizes = new ArrayList<>();


        // Sort the sizes based on the order in the size map
        for (String size : SIZE_ORDER) {
            for (ProductSize productSize : sizes) {
                if (productSize.getSize().equals(size)) {
                    sortedSizes.add(productSize);
                }
            }
        }
        return sortedSizes;
    }
}

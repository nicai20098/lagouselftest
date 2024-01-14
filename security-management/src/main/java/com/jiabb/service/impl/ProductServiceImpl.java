package com.jiabb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiabb.domain.Product;
import com.jiabb.mapper.ProductMapper;
import com.jiabb.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public List<Product> findAll() {
        return null;
    }
}

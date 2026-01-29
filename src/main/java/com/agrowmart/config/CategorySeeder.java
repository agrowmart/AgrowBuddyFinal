package com.agrowmart.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.agrowmart.entity.Category;
import com.agrowmart.repository.CategoryRepository;

@Component
public class CategorySeeder implements CommandLineRunner {

   
    private final CategoryRepository categoryRepo;
    public CategorySeeder(CategoryRepository categoryRepo) {  
        this.categoryRepo = categoryRepo;               
    }

    @Override
    public void run(String... args) {
        createRootIfNotExists("vegetable-root", "Vegetables");
        createRootIfNotExists("seafoodmeat-root", "Seafood & Meat");
        createRootIfNotExists("dairy-root", "Dairy Products");
    }

    private void createRootIfNotExists(String slug, String name) {
        if (categoryRepo.findBySlug(slug).isEmpty()) {  
            Category root = new Category();
            root.setName(name);
            root.setSlug(slug);
            root.setParent(null);
            categoryRepo.save(root);            
            System.out.println("Root category created: " + name + " (" + slug + ")");
        }
    }
}
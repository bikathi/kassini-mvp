package npc.kassinimvp.service;

import npc.kassinimvp.repository.ProductPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductPostService {
    @Autowired
    ProductPostsRepository productPostsRepository;
}

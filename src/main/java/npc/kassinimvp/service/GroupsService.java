package npc.kassinimvp.service;

import npc.kassinimvp.repository.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupsService {
    @Autowired
    GroupsRepository groupsRepository;
}

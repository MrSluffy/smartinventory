package com.smart.inventory.application.data.services.activity;

import com.smart.inventory.application.data.entities.Activity;
import com.smart.inventory.application.data.repository.IActivityRepository;
import com.smart.inventory.application.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService implements IActivityService{

    private final IActivityRepository activityRepository;


    @Autowired
    public ActivityService(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void delete(List<Activity> activities, Utilities utilities) {
        this.activityRepository.deleteAll(activities);
    }

    @Override
    public List<Activity> findAllActivity(Integer id) {
        return activityRepository.search(id);
    }
}

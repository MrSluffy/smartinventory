package com.smart.inventory.application.data.services.activity;

import com.smart.inventory.application.data.entities.Activity;
import com.smart.inventory.application.util.Utilities;

import java.util.List;

public interface IActivityService {

    void delete(List<Activity> employers, Utilities utilities);

    List<Activity> findAllActivity(Integer id);

}

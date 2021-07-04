package com.backend.xplaza.service;

import com.backend.xplaza.model.State;
import com.backend.xplaza.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {
    @Autowired
    private StateRepository stateRepo;

    public void addState(State city) {
        stateRepo.save(city);
    }

    public void updateState(State city) {
        stateRepo.save(city);
    }

    public String getStateNameByID(long id) {
        return stateRepo.getName(id);
    }

    public void deleteState(long id) {
        stateRepo.deleteById(id);
    }

    public List<State> listStates() {
        return stateRepo.findAll();
    }

    public State listState(long id) {
        return stateRepo.findStateById(id);
    }
}

package service;

import model.StatusModel;
import repository.StatusRepository;
import java.util.List;

public class StatusService {
    StatusRepository statusRepository = new StatusRepository();
    public List<StatusModel> getAllStatus(){
        return statusRepository.getAllStatus();
    }
}

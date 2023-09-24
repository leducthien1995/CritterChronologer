package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule findScheduleById(long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Schedule can not found with ID: " + id));
    }

    public Schedule saveSchedule(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    List<Schedule> findByEmployeesId(Long employeeId) {
        return scheduleRepository.findSchedulesByEmployeesId(employeeId);
    }
    List<Schedule> findSchedulesByPetsId(Long petId) {
        return scheduleRepository.findSchedulesByPetsId(petId);
    }
}

package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.customer.Customer;
import com.udacity.jdnd.course3.critter.customer.CustomerService;
import com.udacity.jdnd.course3.critter.employee.Employee;
import com.udacity.jdnd.course3.critter.employee.EmployeeService;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final EmployeeService employeeService;
    private final PetService petService;
    private final ScheduleService scheduleService;
    private final CustomerService customerService;

    public ScheduleController(EmployeeService employeeService, PetService petService, ScheduleService scheduleService, CustomerService customerService) {
        this.employeeService = employeeService;
        this.petService = petService;
        this.scheduleService = scheduleService;
        this.customerService = customerService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertToSchedule(scheduleDTO);
        schedule = scheduleService.saveSchedule(schedule);
        return convertToScheduleDTO(schedule);

    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.findAllSchedules();
        return convertToListScheduleDTO(schedules);
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = scheduleService.findSchedulesByPetsId(petId);
        return convertToListScheduleDTO(schedules);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = scheduleService.findByEmployeesId(employeeId);
        return convertToListScheduleDTO(schedules);
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer = customerService.findCustomerById(customerId);
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        if(customer.getPets() != null){
            for(Pet pet : customer.getPets()){
                List<Schedule> schedulesByPetsId = scheduleService.findSchedulesByPetsId(pet.getId());
                scheduleDTOs.addAll(convertToListScheduleDTO(schedulesByPetsId));
            }
        }
        return scheduleDTOs;
    }

    private ScheduleDTO convertToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        List<Long> petIds = new ArrayList<>();
        if(schedule.getPets() != null){
            schedule.getPets().forEach(pet -> petIds.add(pet.getId()));
        }

        List<Long> employeeIds = new ArrayList<>();
        if(schedule.getEmployees() != null){
            schedule.getEmployees().forEach(employee -> employeeIds.add(employee.getId()));
        }

        scheduleDTO.setPetIds(petIds);
        scheduleDTO.setEmployeeIds(employeeIds);
        return scheduleDTO;
    }

    private Schedule convertToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule  = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);

        List<Pet> pets = new ArrayList<>();
        if(scheduleDTO.getPetIds() != null){
            scheduleDTO.getPetIds().forEach(petId -> pets.add(petService.findPetById(petId)));
        }

        List<Employee> employees = new ArrayList<>();
        if(scheduleDTO.getEmployeeIds() != null){
            scheduleDTO.getEmployeeIds().forEach(employeeId -> employees.add(employeeService.findEmployeeById(employeeId)));
        }

        schedule.setPets(pets);
        schedule.setEmployees(employees);
        return schedule;
    }

    private List<ScheduleDTO> convertToListScheduleDTO(List<Schedule> scheduleLst) {
        List<ScheduleDTO> lstScheduleDTO = new ArrayList<>();
        scheduleLst.forEach(schedule -> lstScheduleDTO.add(convertToScheduleDTO(schedule)));
        return lstScheduleDTO;
    }
}

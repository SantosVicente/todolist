package br.com.santosvicente.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.santosvicente.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/create")
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    taskModel.setIdUser((UUID) idUser);

    var currentDate = LocalDateTime.now();

    if (currentDate.isAfter(taskModel.getStartAt())) {
      return ResponseEntity.status(400).body("A data de início não pode ser anterior a data atual");
    } else if (currentDate.isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(400).body("A data de término não pode ser anterior a data atual");
    } else if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(400).body("A data de início não pode ser posterior a data de término");
    }

    var task = this.taskRepository.save(taskModel);
    return ResponseEntity.status(201).body(task);
  }

  @GetMapping("/get")
  public ResponseEntity get(HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    var data = this.taskRepository.findByIdUser((UUID) idUser);
    return ResponseEntity.status(200).body(data);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
    var idUser = request.getAttribute("idUser");

    var task = this.taskRepository.findById(id).orElse(null);

    Utils.copyNonNullProperties(taskModel, task);

    var data = this.taskRepository.save(task);

    return ResponseEntity.status(200).body(data);
  }
}

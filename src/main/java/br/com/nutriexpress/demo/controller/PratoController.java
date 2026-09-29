package br.com.nutriexpress.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.nutriexpress.demo.dto.PratoRequestDTO;
import br.com.nutriexpress.demo.dto.PratoResponseDTO;
import br.com.nutriexpress.demo.dto.PratoValorRequestDTO;
import br.com.nutriexpress.demo.service.PratoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pratos")
public class PratoController {

    private final PratoService pratoService;

    public PratoController(PratoService pratoService) {
        this.pratoService = pratoService;
    }

    /**
     * Rota: GET /pratos ou GET /pratos?categoria=vegano
     * Descrição: Lista todos os pratos ou filtra por categoria se o parâmetro query 'categoria' for informado.
     * Status HTTP: 200 OK
     */
    @GetMapping
    public ResponseEntity<List<PratoResponseDTO>> listarPratos(@RequestParam(name = "categoria", required = false) String categoria) {
        if (categoria != null && !categoria.isBlank()) {
            return ResponseEntity.ok(pratoService.listarPorCategoria(categoria));
        }
        return ResponseEntity.ok(pratoService.listarTodos());
    }

    /**
     * Rota: GET /pratos/calorias?max=500
     * Descrição: Retorna apenas pratos com até X calorias (Desafio Extra 2).
     * Status HTTP: 200 OK
     */
    @GetMapping("/calorias")
    public ResponseEntity<List<PratoResponseDTO>> filtrarPorCaloriasMax(@RequestParam(name = "max") Integer max) {
        return ResponseEntity.ok(pratoService.filtrarPorCaloriasMax(max));
    }

    /**
     * Rota: GET /pratos/{id}
     * Descrição: Busca um prato pelo seu ID.
     * Status HTTP: 200 OK ou 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<PratoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pratoService.buscarPorId(id));
    }

    /**
     * Rota: POST /pratos
     * Descrição: Cria um novo prato a partir de um DTO validado.
     * Status HTTP: 201 Created
     */
    @PostMapping
    public ResponseEntity<PratoResponseDTO> criarPrato(@Valid @RequestBody PratoRequestDTO dto) {
        PratoResponseDTO pratoCriado = pratoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pratoCriado);
    }

    /**
     * Rota: PUT /pratos/{id}
     * Descrição: Atualiza um prato existente pelo seu ID.
     * Status HTTP: 200 OK ou 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<PratoResponseDTO> atualizarPrato(@PathVariable Long id, @Valid @RequestBody PratoRequestDTO dto) {
        return ResponseEntity.ok(pratoService.atualizar(id, dto));
    }

    /**
     * Rota: PATCH /pratos/{id}/valor
     * Descrição: Atualiza somente o valor (preço) de um prato (Desafio Extra 1).
     * Status HTTP: 200 OK ou 404 Not Found
     */
    @PatchMapping("/{id}/valor")
    public ResponseEntity<PratoResponseDTO> atualizarValor(
            @PathVariable Long id,
            @Valid @RequestBody PratoValorRequestDTO dto) {
        return ResponseEntity.ok(pratoService.atualizarValor(id, dto.valor()));
    }

    /**
     * Rota: DELETE /pratos/{id}
     * Descrição: Remove um prato pelo seu ID.
     * Status HTTP: 204 No Content ou 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPrato(@PathVariable Long id) {
        pratoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}

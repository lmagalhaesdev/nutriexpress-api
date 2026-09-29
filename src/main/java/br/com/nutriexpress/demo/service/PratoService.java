package br.com.nutriexpress.demo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.nutriexpress.demo.dto.PratoRequestDTO;
import br.com.nutriexpress.demo.dto.PratoResponseDTO;
import br.com.nutriexpress.demo.exception.PratoNaoEncontradoException;
import br.com.nutriexpress.demo.model.Prato;
import br.com.nutriexpress.demo.repository.PratoRepository;

@Service
public class PratoService {

    private final PratoRepository pratoRepository;

    public PratoService(PratoRepository pratoRepository) {
        this.pratoRepository = pratoRepository;
    }

    @Transactional
    public PratoResponseDTO criar(PratoRequestDTO dto) {
        // REGRA DE NEGÓCIO PRÓPRIA:
        // Não é permitido cadastrar dois pratos com o mesmo nome na base de dados.
        // Isso previne duplicidade de pratos no menu de delivery.
        if (pratoRepository.existsByNome(dto.nome())) {
            throw new IllegalArgumentException("Já existe um prato cadastrado com o nome: " + dto.nome());
        }

        Prato prato = toEntity(dto);
        Prato pratoSalvo = pratoRepository.save(prato);
        return toDTO(pratoSalvo);
    }

    public List<PratoResponseDTO> listarTodos() {
        return pratoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public PratoResponseDTO buscarPorId(Long id) {
        Prato prato = pratoRepository.findById(id)
                .orElseThrow(() -> new PratoNaoEncontradoException(id));
        return toDTO(prato);
    }

    public List<PratoResponseDTO> listarPorCategoria(String categoria) {
        return pratoRepository.findByCategoria(categoria)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public PratoResponseDTO atualizar(Long id, PratoRequestDTO dto) {
        Prato pratoExistente = pratoRepository.findById(id)
                .orElseThrow(() -> new PratoNaoEncontradoException(id));

        // REGRA DE NEGÓCIO PRÓPRIA:
        // Na atualização, valida se o novo nome já pertence a outro prato existente.
        if (pratoRepository.existsByNomeAndIdNot(dto.nome(), id)) {
            throw new IllegalArgumentException("Já existe outro prato cadastrado com o nome: " + dto.nome());
        }

        pratoExistente.setNome(dto.nome());
        pratoExistente.setDescricao(dto.descricao());
        pratoExistente.setValor(dto.valor());
        pratoExistente.setCategoria(dto.categoria());
        pratoExistente.setCalorias(dto.calorias());
        pratoExistente.setQuantidade(dto.quantidade());
        pratoExistente.setUnidadeMedida(dto.unidadeMedida());

        Prato pratoAtualizado = pratoRepository.save(pratoExistente);
        return toDTO(pratoAtualizado);
    }

    @Transactional
    public void remover(Long id) {
        Prato pratoExistente = pratoRepository.findById(id)
                .orElseThrow(() -> new PratoNaoEncontradoException(id));
        pratoRepository.delete(pratoExistente);
    }

    // Desafio Extra 1: PATCH /pratos/{id}/valor
    @Transactional
    public PratoResponseDTO atualizarValor(Long id, BigDecimal novoValor) {
        Prato pratoExistente = pratoRepository.findById(id)
                .orElseThrow(() -> new PratoNaoEncontradoException(id));
        pratoExistente.setValor(novoValor);
        Prato pratoAtualizado = pratoRepository.save(pratoExistente);
        return toDTO(pratoAtualizado);
    }

    // Desafio Extra 2: GET /pratos/calorias?max=500
    public List<PratoResponseDTO> filtrarPorCaloriasMax(Integer max) {
        return pratoRepository.findAll()
                .stream()
                .filter(prato -> prato.getCalorias() != null && prato.getCalorias() <= max)
                .map(this::toDTO)
                .toList();
    }

    // Métodos privados de conversão exigidos no PDF (toEntity e toDTO)
    private Prato toEntity(PratoRequestDTO dto) {
        return new Prato(
                dto.nome(),
                dto.descricao(),
                dto.valor(),
                dto.categoria(),
                dto.calorias(),
                dto.quantidade(),
                dto.unidadeMedida()
        );
    }

    private PratoResponseDTO toDTO(Prato entity) {
        return new PratoResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getValor(),
                entity.getCategoria(),
                entity.getCalorias(),
                entity.getQuantidade(),
                entity.getUnidadeMedida()
        );
    }
}

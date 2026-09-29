package br.com.nutriexpress.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.nutriexpress.demo.model.Prato;

@Repository
public interface PratoRepository extends JpaRepository<Prato, Long> {

    List<Prato> findByCategoria(String categoria);

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);
}

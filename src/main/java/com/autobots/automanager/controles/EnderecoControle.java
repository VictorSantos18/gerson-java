package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.AdicionadorLink;
import com.autobots.automanager.modelo.AdicionadorLinkEndereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.modelo.EnderecoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
public class EnderecoControle {
	@Autowired
	private ClienteRepositorio repositorio;
	@Autowired
	private EnderecoRepositorio repositorioEndereco;
	@Autowired
	private AdicionadorLinkEndereco adicionadorLink;
	@Autowired
	private EnderecoSelecionador enderecoSelecionador;
	@Autowired
	private EnderecoAtualizador enderecoAtualizador;

	@GetMapping("/endereco/{id}")
	public ResponseEntity<Endereco> obterEndereco(@PathVariable long id) {
		List<Endereco> enderecos = repositorioEndereco.findAll();
		Endereco endereco = enderecoSelecionador.selecionar(enderecos, id);
		if (enderecos == null) {
			ResponseEntity<Endereco> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(enderecos);
			ResponseEntity<Endereco> resposta = new ResponseEntity<Endereco>(endereco, HttpStatus.FOUND);
			return resposta;
		}
	}

	@GetMapping("/enderecos")
	public ResponseEntity<List<Endereco>> obterEndereco() {
		List<Endereco> enderecos = repositorioEndereco.findAll();
		if (enderecos.isEmpty()) {
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(enderecos);
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(enderecos, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PutMapping("/atualizarEndereco")
	public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Endereco endereco = repositorioEndereco.getById(atualizacao.getId());
		if (endereco != null) {
			EnderecoAtualizador enderecoAtualizador = new EnderecoAtualizador();
			enderecoAtualizador.atualizar(endereco, atualizacao);
			repositorioEndereco.save(endereco);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/excluirEndereco/{id}")
	public ResponseEntity<?> excluirEndereco(@RequestBody Endereco exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Endereco endereco = repositorioEndereco.getById(exclusao.getId());
		if (endereco != null) {
			repositorioEndereco.delete(endereco);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}

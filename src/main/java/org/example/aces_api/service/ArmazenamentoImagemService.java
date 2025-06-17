package org.example.aces_api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class ArmazenamentoImagemService {

    private final Path diretorioRaiz = Paths.get("uploads/imagens");

    public ArmazenamentoImagemService() {
        try {
            Files.createDirectories(diretorioRaiz);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar o diretório de armazenamento de imagens", e);
        }
    }

    /**
     * Armazena a imagem no sistema de arquivos e retorna o nome do arquivo
     */
    public String armazenarImagem(MultipartFile arquivo) {
        try {
            if (arquivo == null || arquivo.isEmpty()) {
                throw new RuntimeException("Falha ao armazenar arquivo vazio");
            }

            String nomeArquivo = UUID.randomUUID().toString() + "_" + arquivo.getOriginalFilename();
            Path destinoArquivo = this.diretorioRaiz.resolve(Paths.get(nomeArquivo)).normalize();

            Files.copy(arquivo.getInputStream(), destinoArquivo);

            return nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao armazenar arquivo", e);
        }
    }

    /**
     * Converte a imagem para Base64 e retorna a string codificada
     */
    public String converterImagemParaBase64(MultipartFile arquivo) {
        try {
            if (arquivo == null || arquivo.isEmpty()) {
                throw new RuntimeException("Não é possível converter um arquivo vazio para Base64");
            }

            byte[] bytes = arquivo.getBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter imagem para Base64", e);
        }
    }

    /**
     * Armazena a imagem e também retorna sua representação em Base64
     * @return Um array onde o primeiro elemento é o nome do arquivo e o segundo é a string Base64
     */
    public String[] armazenarEConverterImagem(MultipartFile arquivo) {
        String nomeArquivo = armazenarImagem(arquivo);
        String base64 = converterImagemParaBase64(arquivo);
        return new String[]{nomeArquivo, base64};
    }

    /**
     * Carrega uma imagem do sistema de arquivos
     */
    public Path carregarImagem(String nomeArquivo) {
        return diretorioRaiz.resolve(nomeArquivo);
    }

    /**
     * Exclui uma imagem do sistema de arquivos
     */
    public void excluirImagem(String nomeArquivo) {
        try {
            if (nomeArquivo == null || nomeArquivo.isEmpty()) {
                return; // Não faz nada se o nome do arquivo for vazio
            }

            Path arquivo = carregarImagem(nomeArquivo);
            Files.deleteIfExists(arquivo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao excluir arquivo", e);
        }
    }

    /**
     * Converte um arquivo no sistema de arquivos para Base64
     */
    public String arquivoParaBase64(String nomeArquivo) {
        try {
            if (nomeArquivo == null || nomeArquivo.isEmpty()) {
                throw new RuntimeException("Nome de arquivo inválido");
            }

            Path arquivo = carregarImagem(nomeArquivo);
            byte[] bytes = Files.readAllBytes(arquivo);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter arquivo para Base64", e);
        }
    }
}

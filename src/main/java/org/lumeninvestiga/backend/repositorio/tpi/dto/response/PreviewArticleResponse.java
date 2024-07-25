package org.lumeninvestiga.backend.repositorio.tpi.dto.response;

public record PreviewArticleResponse(
        String title,
        String author,
        String resume,
        int likeCounter
) {
}

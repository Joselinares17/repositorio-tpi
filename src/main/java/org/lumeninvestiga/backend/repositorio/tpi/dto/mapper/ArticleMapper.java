package org.lumeninvestiga.backend.repositorio.tpi.dto.mapper;

import org.lumeninvestiga.backend.repositorio.tpi.dto.response.ArticleDetailDTO;
import org.lumeninvestiga.backend.repositorio.tpi.dto.response.ArticleResponse;
import org.lumeninvestiga.backend.repositorio.tpi.entities.data.Article;
import org.lumeninvestiga.backend.repositorio.tpi.entities.data.ArticleDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    @Mapping(target = "articleDetailDTO", source = "articleDetail")
    ArticleResponse toArticleResponse(Article article);

    @Mapping(target = "area", source = "area")
    @Mapping(target = "subArea", source = "subArea")
    @Mapping(target = "period", source = "period")
    @Mapping(target = "ods", source = "ods")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "advisor", source = "advisor")
    @Mapping(target = "resume", source = "resume")
    @Mapping(target = "keywords", source = "keywords")
    ArticleDetailDTO articleDetailToArticleDetailDTO(ArticleDetail articleDetail);

}

package org.lumeninvestiga.backend.repositorio.tpi;

import org.lumeninvestiga.backend.repositorio.tpi.controllers.AuthController;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.UserRegistrationRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.response.AuthResponse;
import org.lumeninvestiga.backend.repositorio.tpi.entities.data.Article;
import org.lumeninvestiga.backend.repositorio.tpi.entities.data.ArticleDetail;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.Review;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.User;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.UserDetail;
import org.lumeninvestiga.backend.repositorio.tpi.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication

public class RepositorioTpiApplication {
	private static final int MIN_PDF_SIZE = 1024; // 1KB
	private static final int MAX_PDF_SIZE = 10 * 1024 * 1024; // 10MB

	public static void main(String[] args) {
		SpringApplication.run(RepositorioTpiApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, ArticleRepository articleRepository,
										ReviewRepository reviewRepository, AuthController authController) throws IOException {

		User user = new User();
		user.setUsername("20114234");
		user.setPassword("1234");
		User user1 = new User();
		user1.setUsername("20223412");
		user1.setPassword("nosequeponer");

		UserDetail userD = new UserDetail();
		userD.setName("Pedro");
		userD.setLastName("Sanchez");
		userD.setEmailAddress("1234@example.com");

		userD.setUser(user);
		user.setUserDetail(userD);

		UserDetail userD1 = new UserDetail();
		userD1.setName("Juan");
		userD1.setLastName("Rodriguez");
		userD1.setEmailAddress("12341234@example.com");

		userD1.setUser(user1);
		user1.setUserDetail(userD1);

		userRepository.save(user);
		userRepository.save(user1);

		ArticleDetail articleDetail = new ArticleDetail();
		articleDetail.setTitle("MachineLearning");
		articleDetail.setAuthor("Juan pedro");
		articleDetail.setAdvisor("Roberto Rojas");
		articleDetail.setPeriod("2018-2");
		articleDetail.setOds("1");
		articleDetail.setKeywords("IA, MachineLearning, IOT");
		articleDetail.setArea("IA");
		articleDetail.setSubArea("Supervisado");
		articleDetail.setResume("Este trabajo trata sobre la investigacion de modelos de machine learning...");


		Article article = new Article();
		article.setUser(user);
		article.setArticleDetail(articleDetail);

		articleRepository.save(article);

		Review review = new Review();
		review.setComment("Muy buen trabajo, saludos");
		review.setUser(user);
		review.setArticle(article);
		user.addReview(review);

		reviewRepository.save(review);

		Review review1 = new Review();
		review1.setComment("Muy buen trabajo me encantó");
		review1.setUser(user1);
		review1.setArticle(article);
		user1.addReview(review1);

		reviewRepository.save(review1);


		Review review2 = new Review();
		review2.setComment("Ya he visto este trabajo en otro lado, lo reportaré");

		for(Article item : getData()){
			item.setUser(user);
			articleRepository.save(item);
			System.out.println("Articulo: " + item.getArticleDetail().getTitle() + " guardado." );
		}

		ResponseEntity<Optional<AuthResponse>> response = authController.register(
				new UserRegistrationRequest("Jose", "Linares","20184082",
						"20184082@aloe.ulima.edu.pe", "admin1234")
		);

		System.out.println(response.getBody().get().token());



		return args -> {
			System.out.println("Usuario guardado " + user.getUserDetail().getName());

			System.out.println("Usuario guardado " + user1.getUserDetail().getName());
		};
	}

	public static byte[] generateRandomPdfContent() {
		SecureRandom random = new SecureRandom();
		int pdfSize = random.nextInt(MAX_PDF_SIZE - MIN_PDF_SIZE + 1) + MIN_PDF_SIZE;

		byte[] pdfContent = new byte[pdfSize];
		random.nextBytes(pdfContent);

		return pdfContent;
	}

	public static List<Article> getData() {
		List<Article> articles = new ArrayList<>();

		// Artículo 1
		ArticleDetail detail1 = new ArticleDetail();
		detail1.setArea("Ciencias de la Computación");
		detail1.setSubArea("Inteligencia Artificial");
		detail1.setPeriod("2018-2");
		detail1.setOds("9");
		detail1.setTitle("Una Introducción al Aprendizaje Automático");
		detail1.setAuthor("Juan Pérez");
		detail1.setAdvisor("Dr. García");
		detail1.setResume("Este artículo introduce los conceptos básicos del aprendizaje automático...");
		detail1.setKeywords("Aprendizaje Automático, IA, Algoritmos");

		Article article1 = new Article();
		article1.setArticleDetail(detail1);

		// Artículo 2
		ArticleDetail detail2 = new ArticleDetail();
		detail2.setArea("Ciencias Ambientales");
		detail2.setSubArea("Cambio Climático");
		detail2.setPeriod("2019-0");
		detail2.setOds("13");
		detail2.setTitle("El Impacto del Cambio Climático en las Zonas Costeras");
		detail2.setAuthor("Ana López");
		detail2.setAdvisor("Dr. Verde");
		detail2.setResume("Este artículo examina los efectos del cambio climático en las regiones costeras...");
		detail2.setKeywords("Cambio Climático, Zonas Costeras, Ciencias Ambientales");

		Article article2 = new Article();
		article2.setArticleDetail(detail2);

		// Artículo 3
		ArticleDetail detail3 = new ArticleDetail();
		detail3.setArea("Ciencias Médicas");
		detail3.setSubArea("Genética");
		detail3.setPeriod("2019-0");
		detail3.setOds("3");
		detail3.setTitle("Avances en Ingeniería Genética");
		detail3.setAuthor("Laura Gómez");
		detail3.setAdvisor("Dr. Blanco");
		detail3.setResume("Este artículo explora los avances recientes en el campo de la ingeniería genética...");
		detail3.setKeywords("Ingeniería Genética, Ciencias Médicas, CRISPR");

		Article article3 = new Article();
		article3.setArticleDetail(detail3);

		// Artículo 4
		ArticleDetail detail4 = new ArticleDetail();
		detail4.setArea("Física");
		detail4.setSubArea("Mecánica Cuántica");
		detail4.setPeriod("2019-0");
		detail4.setOds("4");
		detail4.setTitle("Entendiendo el Entrelaçamento Cuántico");
		detail4.setAuthor("Carlos Martínez");
		detail4.setAdvisor("Dr. Cuántico");
		detail4.setResume("Este artículo proporciona un análisis profundo del entrelaçamento cuántico...");
		detail4.setKeywords("Mecánica Cuántica, Física, Entrelaçamento Cuántico");

		Article article4 = new Article();
		article4.setArticleDetail(detail4);

		// Artículo 5
		ArticleDetail detail5 = new ArticleDetail();
		detail5.setArea("Economía");
		detail5.setSubArea("Economía Conductual");
		detail5.setPeriod("2022-1");
		detail5.setOds("8");
		detail5.setTitle("La Psicología de las Decisiones Económicas");
		detail5.setAuthor("María Fernández");
		detail5.setAdvisor("Dr. Conductual");
		detail5.setResume("Este artículo explora los factores psicológicos que influyen en las decisiones económicas...");
		detail5.setKeywords("Economía Conductual, Psicología, Toma de Decisiones");

		Article article5 = new Article();
		article5.setArticleDetail(detail5);

		articles.add(article1);
		articles.add(article2);
		articles.add(article3);
		articles.add(article4);
		articles.add(article5);

		return articles;
	}
}

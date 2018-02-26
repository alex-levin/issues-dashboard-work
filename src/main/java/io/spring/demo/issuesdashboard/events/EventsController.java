package io.spring.demo.issuesdashboard.events;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import io.spring.demo.issuesdashboard.github.GithubClient;
import io.spring.demo.issuesdashboard.github.RepositoryEvent;

@Controller
public class EventsController {

	private final GithubClient githubClient;

	private final GithubProjectRepository repository;

	public EventsController(GithubClient githubClient, GithubProjectRepository repository) {
		this.githubClient = githubClient;
		this.repository = repository;
	}

	/**
	 * Fetches latest events for the repo
	 * Endpoint URL: http://localhost:8080/events/spring-boot
	 * @param repoName
	 * @return array of the events
	 */
	@GetMapping("/events/{repoName}")
	@ResponseBody
	public RepositoryEvent[] fetchEvents(@PathVariable String repoName) {
		// h2 db has a row with ID:1, ORG_NAME:spring_projects, REPO_NAME:spring-boot
		// DB is populated by Flyway from V1.0.1_Insert_data.sql in the 
		// resources/db/migration folder.
		// The endpoint URL: https://api.github.com/repos/spring-projects/spring-boot/issues/events
		GithubProject project = this.repository.findByRepoName(repoName);
		return this.githubClient
				.fetchEvents(project.getOrgName(), project.getRepoName()).getBody();
	}

}

package org.opengroup.osdu.crs.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthCheckTests {

	private HealthCheck sut;

	@BeforeEach
	public void setup() {
		this.sut = new HealthCheck();
	}

	@Test
	public void should_returnHttp200_when_checkLiveness() {
		assertEquals(HttpStatus.OK, this.sut.livenessCheck().getStatusCode());
	}

	@Test
	public void should_returnHttp200_when_checkReadiness() {
		assertEquals(HttpStatus.OK, this.sut.readinessCheck().getStatusCode());
	}
}
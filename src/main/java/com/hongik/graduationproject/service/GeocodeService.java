package com.hongik.graduationproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.hongik.graduationproject.domain.dto.geocode.NaverGeocodeResponse;
import com.hongik.graduationproject.domain.dto.video.Coordinate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GeocodeService {
	private final RestClient restClient;

	@Value("${naver.geocode.url}")
	private String naverGeocodeUrl;
	@Value("${naver.geocode.id}")
	private String naverGeocodeId;
	@Value("${naver.geocode.secret}")
	private String naverGeocodeSecret;

	public Coordinate getCoordinateByAddress(String address) {
		NaverGeocodeResponse result = restClient.get()
			.uri(naverGeocodeUrl + "?query=" + address)
			.header("X-NCP-APIGW-API-KEY-ID", naverGeocodeId)
			.header("X-NCP-APIGW-API-KEY", naverGeocodeSecret)
			.retrieve()
			.body(NaverGeocodeResponse.class);

		System.out.println("result = " + result);

		if (result.getAddresses() == null || result.getAddresses().isEmpty()) {
			return null;
		}

		return new Coordinate(
			Double.valueOf(result.getAddresses().get(0).getY()),
			Double.valueOf(result.getAddresses().get(0).getX())
		);
	}
}

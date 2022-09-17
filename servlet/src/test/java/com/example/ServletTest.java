package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ServletTest {

	@Test
	void testSharedCounter() throws Exception {
		// 톰캣 서버 시작
		final var tomcatStarter = TestHttpUtils.createTomcatStarter();
		tomcatStarter.start();

		// shared-counter 페이지를 3번 호출한다.
		final var PATH = "/shared-counter";
		TestHttpUtils.send(PATH);
		TestHttpUtils.send(PATH);
		final var response = TestHttpUtils.send(PATH);

		// 톰캣 서버 종료
		tomcatStarter.stop();

		assertThat(response.statusCode()).isEqualTo(200);

		// expected를 0이 아닌 올바른 값으로 바꿔보자.
		// 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        /**
         * 서블릿을 호출할 때 가지고 있는 인스턴스 변수가 공유되어서 해당 클래스가 호출될 때마다
         * 증감되어 호출 횟수인 3번이 출력된다.
         *
         */
		assertThat(Integer.parseInt(response.body())).isEqualTo(3);
	}

	@Test
	void testLocalCounter() throws Exception {
		// 톰캣 서버 시작
		final var tomcatStarter = TestHttpUtils.createTomcatStarter();
		tomcatStarter.start();

		// local-counter 페이지를 3번 호출한다.
		final var PATH = "/local-counter";
		TestHttpUtils.send(PATH);
		TestHttpUtils.send(PATH);
		final var response = TestHttpUtils.send(PATH);

		// 톰캣 서버 종료
		tomcatStarter.stop();

		assertThat(response.statusCode()).isEqualTo(200);

		// expected를 0이 아닌 올바른 값으로 바꿔보자.
		// 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        /**
         * 지역 변수는 해당 메서드가 종료되면 소멸되기 때문에 데이터가 공유되지 않는다.
         * 서블릿에 경우는 최초 로드될 때 인스턴스가 생겨나고, 프로그램 생명주기가 끝날 때
         * 소멸되기 때문에 프로그램이 실행중인 상태에서 해당 메서드를 여러번 호출해서 사용할 수 있다.
         * 밖에 전역으로 인스턴스가 존재하고 그 인스턴스를 조작하는 행위가 메서드 내부에 있게 된다면
         * 값을 공유하게 되어 response 를 받을 때마다 같은 값을 기대할 수 없다.
         */
		assertThat(Integer.parseInt(response.body())).isEqualTo(1);
	}
}

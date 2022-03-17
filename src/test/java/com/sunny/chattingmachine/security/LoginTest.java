package com.sunny.chattingmachine.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunny.chattingmachine.domain.account.Account;
import com.sunny.chattingmachine.domain.account.AccountRole;
import com.sunny.chattingmachine.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    EntityManager entityManager;

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    ObjectMapper objectMapper = new ObjectMapper();

    private static String KEY_ACCOUNTID = "accountId";
    private static String KEY_PASSWORD = "password";
    private static String ACCOUNTID = "accountId";
    private static String PASSWORD = "123456789";
    private static String LOGIN_URL = "/login";

    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private void clear() {
        entityManager.flush();
        entityManager.clear();
    }

    @BeforeEach
    private void init() {
        accountRepository.save(Account.builder()
                .accountId(ACCOUNTID)
                .password(passwordEncoder.encode(PASSWORD))
                .name("account1")
                .nickName("nick1")
                .age(30)
                .accountRole(AccountRole.USER)
                .build());
        clear();
    }

    private Map getIdPwMap(String accountId, String password) {
        Map<String, String> map = new HashMap<>();
        map.put(KEY_ACCOUNTID, accountId);
        map.put(KEY_PASSWORD, password);
        return map;
    }

    private ResultActions perform(String url, MediaType mediaType, Map idPwMap) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(mediaType)
                .content(objectMapper.writeValueAsString(idPwMap)));
    }

    @Test
    public void loginSuccess() throws Exception {
        Map<String, String> map = getIdPwMap(ACCOUNTID, PASSWORD);
        MvcResult result = perform(LOGIN_URL, APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getHeader(accessHeader)).isNotNull();
        assertThat(result.getResponse().getHeader(refreshHeader)).isNotNull();
    }

    @Test
    public void loginFailWrongId() throws Exception {
        Map<String, String> map = getIdPwMap(ACCOUNTID + " wrongId1", PASSWORD);

        MvcResult result = perform(LOGIN_URL, APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getHeader(accessHeader)).isNotNull();
        assertThat(result.getResponse().getHeader(refreshHeader)).isNotNull();
    }

    @Test
    public void loginFailWrongPw() throws Exception {
        Map<String, String> map = getIdPwMap(ACCOUNTID, PASSWORD + " wrongPw1");

        MvcResult result = perform(LOGIN_URL, APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getHeader(accessHeader)).isNotNull();
        assertThat(result.getResponse().getHeader(refreshHeader)).isNotNull();
    }

    @Test
    public void loginFailWrongUrlForbidden() throws Exception {
        Map<String, String> map = getIdPwMap(ACCOUNTID, PASSWORD);

        perform(LOGIN_URL + " wrongUrl1", APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void loginFailNotJson200() throws Exception {
        Map<String, String> map = getIdPwMap(ACCOUNTID, PASSWORD);

        perform(LOGIN_URL, MediaType.APPLICATION_FORM_URLENCODED, map)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void loginFailHttpMethodGetNotFound() throws Exception {
        Map<String, String> map = getIdPwMap(ACCOUNTID, PASSWORD);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(objectMapper.writeValueAsString(map)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void loginFailHttpMethodPutNotFound() throws Exception {
        Map<String, String> map = getIdPwMap(ACCOUNTID, PASSWORD);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(objectMapper.writeValueAsString(map)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}

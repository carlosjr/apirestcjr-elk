package br.com.cjr.apirest.elk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import com.google.gson.Gson;

@Component
public class LogRequestFilter extends OncePerRequestFilter implements Ordered {

	private int order = Ordered.LOWEST_PRECEDENCE - 8;

	private static final String NAO_DISPONIVEL_TRUE = "{\"naoDisponivel\":true}";
	private static final Logger LOGGER = LogManager.getLogger(LogRequestFilter.class);
	private static final Gson GSON = new Gson();
	private static final String REQUEST = "request";
	private static final String RESPONSE = "response";

	@Value("${info.app.name}")
	private String project;

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

		HttpServletResponse responseToUse = response;
		if (!(response instanceof ContentCachingResponseWrapper)) {
			responseToUse = new ContentCachingResponseWrapper(response);
		}
		filterChain.doFilter(wrappedRequest, responseToUse);
		int status = responseToUse.getStatus();

		Map<String, Object> trace = getTrace(wrappedRequest, status);

		getBody(wrappedRequest, trace);
		getBodyResponse(responseToUse, trace);
		logTrace(trace);
	}

	private void getBody(ContentCachingRequestWrapper request, Map<String, Object> trace) {
		ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
		if (wrapper != null) {
			String payload;
			if ((wrapper.getContentType() != null) && !wrapper.getContentType().contains("multipart")) {
				byte[] buf = wrapper.getContentAsByteArray();
				if (buf.length > 0) {
					try {
						payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
					} catch (UnsupportedEncodingException ex) {
						payload = NAO_DISPONIVEL_TRUE;
					}
				} else {
					payload = NAO_DISPONIVEL_TRUE;
				}
				trace.put(REQUEST, payload);
			} else {
				payload = NAO_DISPONIVEL_TRUE;
				trace.put(REQUEST, payload);
			}
		}
	}

	private void getBodyResponse(HttpServletResponse response, Map<String, Object> trace) {
		ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response,
				ContentCachingResponseWrapper.class);
		if (responseWrapper != null) {
			String payload;
			if ((responseWrapper.getContentType() != null) && !responseWrapper.getContentType().contains("multipart")) {
				try {
					byte[] buf = responseWrapper.getContentAsByteArray();
					responseWrapper.copyBodyToResponse();
					if (buf.length > 0) {
						payload = new String(buf, 0, buf.length, response.getCharacterEncoding());
					} else {
						payload = NAO_DISPONIVEL_TRUE;
					}
					trace.put(RESPONSE, validatePayloadResponse(payload));
				} catch (IOException e) {
					LOGGER.error("Erro ao buscar resposta para log.", e);
				}
			} else {
				trace.put(RESPONSE, NAO_DISPONIVEL_TRUE);
			}
		}
	}
	
	// TODO adicionar outros formatos nao necessarios para o log. Buffer do arquivo estava estouro limit size permitido para o logo no GCP.
	private String validatePayloadResponse(String payload) {
		if(payload.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return NAO_DISPONIVEL_TRUE;
		} 
		return payload;
	}

	private void logTrace(Map<String, Object> trace) {
		String json = GSON.toJson(trace);
		LOGGER.info(json);
	}

	protected Map<String, Object> getTrace(HttpServletRequest request, int status) {

//		Principal principal = request.getUserPrincipal();
		
		String principal = ObjectUtils.isEmpty(request.getAttribute("usuarioLogado")) 
				? null : request.getAttribute("usuarioLogado").toString();

		Map<String, Object> trace = new LinkedHashMap<>();
		trace.put("method", request.getMethod());
		trace.put("path", request.getRequestURI());
		if (principal != null) {
			trace.put("user", principal);
		} else {
			trace.put("user", "Anônimo");
		}
		trace.put("query", request.getQueryString());
		trace.put("statusCode", status);
		trace.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		trace.put("project", project.substring(1));
		return trace;
	}

}

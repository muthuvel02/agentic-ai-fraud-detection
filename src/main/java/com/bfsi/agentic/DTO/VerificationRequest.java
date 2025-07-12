package com.bfsi.agentic.DTO;

public class VerificationRequest {
    private String token;
    private String answer;
    private String action;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}

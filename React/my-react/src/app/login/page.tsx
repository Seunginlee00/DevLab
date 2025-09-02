"use client";

import React, { useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { login } from "@/api/auth";

export default function LoginPage() {
  const router = useRouter();
  const sp = useSearchParams();
  const [userId, setUserId] = useState("");
  const [pw, setPw] = useState("");
  const [showPw, setShowPw] = useState(false);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErr(null);
    setLoading(true);
    const { success, message } = await login(userId.trim(), pw);
    setLoading(false);
    if (!success) { setErr(message ?? "아이디/비밀번호를 확인해 주세요."); return; }
    // 리다이렉트 복원
    const redirect = sp.get("redirect") || "/";
    router.replace(redirect);
  };

  const can = userId.trim().length > 0 && pw.length >= 8 && !loading;

  return (
      <div style={{ maxWidth: 380, margin: "64px auto", padding: 24, borderRadius: 16, border: "1px solid #eee", boxShadow: "0 6px 24px rgba(0,0,0,.08)" }}>
        <h1 style={{ margin: 0, fontSize: 22, fontWeight: 700 }}>로그인</h1>
        <p style={{ color: "#666", marginTop: 8, marginBottom: 24 }}>아이디와 비밀번호를 입력해 주세요.</p>

        <form onSubmit={onSubmit}>
          <label style={{ display: "block", fontSize: 14, marginBottom: 8 }}>아이디</label>
          <input
              value={userId} onChange={e => setUserId(e.target.value)}
              autoComplete="username" placeholder="아이디"
              style={{ width: "100%", padding: "10px 12px", borderRadius: 10, border: "1px solid #ddd", marginBottom: 16 }}
          />

          <label style={{ display: "block", fontSize: 14, marginBottom: 8 }}>비밀번호</label>
          <div style={{ position: "relative", marginBottom: 12 }}>
            <input
                value={pw} onChange={e => setPw(e.target.value)}
                type={showPw ? "text" : "password"}
                autoComplete="current-password" placeholder="비밀번호 (8자 이상)"
                style={{ width: "100%", padding: "10px 40px 10px 12px", borderRadius: 10, border: "1px solid #ddd" }}
            />
            <button type="button" onClick={() => setShowPw(v => !v)} style={{ position: "absolute", right: 8, top: "50%", transform: "translateY(-50%)", border: "none", background: "transparent", cursor: "pointer" }}>
              {showPw ? "🙈" : "👁️"}
            </button>
          </div>

          {err && <div role="alert" style={{ background: "#fff3f3", color: "#b40000", border: "1px solid #ffd7d7", padding: 10, borderRadius: 8, marginBottom: 12 }}>{err}</div>}

          <button type="submit" disabled={!can}
                  style={{ width: "100%", padding: 12, borderRadius: 10, border: "none", background: can ? "#0b5fff" : "#aac3ff", color: "#fff", fontWeight: 700 }}>
            {loading ? "로그인 중..." : "로그인"}
          </button>
        </form>
      </div>
  );
}

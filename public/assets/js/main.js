/* ==========================================================================
   E-Consultation Sentiment Analysis - Shared JS
   ========================================================================== */

/* ---------- Sidebar toggle (mobile) ---------- */
function initSidebar() {
  const sidebar = document.getElementById("sidebar");
  const toggle = document.getElementById("sidebarToggle");
  const backdrop = document.getElementById("sidebarBackdrop");
  if (!sidebar || !toggle) return;

  const open = () => { sidebar.classList.add("show"); backdrop && backdrop.classList.add("show"); };
  const close = () => { sidebar.classList.remove("show"); backdrop && backdrop.classList.remove("show"); };

  toggle.addEventListener("click", () => {
    sidebar.classList.contains("show") ? close() : open();
  });
  backdrop && backdrop.addEventListener("click", close);
}

/* ---------- Navbar scroll shadow ---------- */
function initNavbarScroll() {
  const nav = document.querySelector(".navbar-landing");
  if (!nav) return;
  window.addEventListener("scroll", () => {
    nav.style.boxShadow = window.scrollY > 20
      ? "0 8px 24px rgba(15,23,42,0.10)"
      : "0 1px 3px rgba(15,23,42,0.08)";
  });
}

/* ---------- Scroll reveal ---------- */
function initReveal() {
  const items = document.querySelectorAll(".reveal");
  if (!items.length) return;
  const obs = new IntersectionObserver((entries) => {
    entries.forEach((e) => { if (e.isIntersecting) e.target.classList.add("visible"); });
  }, { threshold: 0.15 });
  items.forEach((i) => obs.observe(i));
}

/* ---------- Toast helper ---------- */
function showToast(message, type = "success") {
  let container = document.getElementById("toastContainer");
  if (!container) {
    container = document.createElement("div");
    container.id = "toastContainer";
    container.className = "toast-container position-fixed top-0 end-0 p-3";
    container.style.zIndex = "1090";
    document.body.appendChild(container);
  }
  const colors = { success: "text-bg-success", danger: "text-bg-danger", warning: "text-bg-warning", info: "text-bg-primary" };
  const icons = { success: "bi-check-circle-fill", danger: "bi-x-circle-fill", warning: "bi-exclamation-triangle-fill", info: "bi-info-circle-fill" };
  const el = document.createElement("div");
  el.className = `toast align-items-center ${colors[type] || colors.success} border-0`;
  el.setAttribute("role", "alert");
  el.style.borderRadius = "12px";
  el.innerHTML = `
    <div class="d-flex">
      <div class="toast-body d-flex align-items-center gap-2">
        <i class="bi ${icons[type] || icons.success}"></i> ${message}
      </div>
      <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
    </div>`;
  container.appendChild(el);
  const toast = new bootstrap.Toast(el, { delay: 3200 });
  toast.show();
  el.addEventListener("hidden.bs.toast", () => el.remove());
}

/* ---------- Animated counters ---------- */
function initCounters() {
  const els = document.querySelectorAll("[data-count]");
  if (!els.length) return;
  const run = (el) => {
    const target = parseFloat(el.getAttribute("data-count"));
    const suffix = el.getAttribute("data-suffix") || "";
    const decimals = (target % 1 !== 0) ? 1 : 0;
    let cur = 0;
    const step = target / 60;
    const tick = () => {
      cur += step;
      if (cur >= target) { el.textContent = target.toLocaleString() + suffix; return; }
      el.textContent = cur.toFixed(decimals).toLocaleString() + suffix;
      requestAnimationFrame(tick);
    };
    tick();
  };
  const obs = new IntersectionObserver((entries) => {
    entries.forEach((e) => { if (e.isIntersecting) { run(e.target); obs.unobserve(e.target); } });
  }, { threshold: 0.4 });
  els.forEach((el) => obs.observe(el));
}

/* ---------- Star rating input ---------- */
function initStarInput() {
  const groups = document.querySelectorAll(".star-input");
  groups.forEach((group) => {
    const stars = group.querySelectorAll("i");
    const hidden = group.parentElement.querySelector("input[type=hidden]");
    stars.forEach((star, idx) => {
      star.addEventListener("click", () => {
        stars.forEach((s, i) => {
          s.classList.toggle("active", i <= idx);
          s.className = i <= idx ? "bi bi-star-fill active" : "bi bi-star";
        });
        if (hidden) hidden.value = idx + 1;
        group.dispatchEvent(new CustomEvent("ratingchange", { detail: idx + 1 }));
      });
    });
  });
}

/* ---------- Chart theme defaults ---------- */
function chartDefaults() {
  if (window.Chart) {
    Chart.defaults.font.family = "Poppins, sans-serif";
    Chart.defaults.color = "#64748b";
    Chart.defaults.plugins.legend.labels.usePointStyle = true;
    Chart.defaults.plugins.legend.labels.boxWidth = 8;
  }
}

document.addEventListener("DOMContentLoaded", () => {
  initSidebar();
  initNavbarScroll();
  initReveal();
  initCounters();
  initStarInput();
  chartDefaults();

  // Highlight current active nav (fallback)
  const path = window.location.pathname.split("/").pop();
  document.querySelectorAll(".sidebar-nav a").forEach((a) => {
    if (a.getAttribute("href") === path) a.classList.add("active");
  });
});

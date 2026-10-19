document.addEventListener('DOMContentLoaded', () => {

  const MAIN_SITE = 'https://www.webguruji.online';

  // Remove existing hardcoded footers
  document.querySelectorAll('footer.footer, footer.hg-global-footer, footer.relative.z-20').forEach(el => el.remove());
  const oldFooterPlaceholder = document.getElementById('footer-placeholder');

  const currentYear = new Date().getFullYear();

  const footerHTML = `
    <footer class="hg-global-footer">
      <div class="hg-footer-grid">
        <div class="hg-footer-brand">
          <div class="hg-footer-brand-title">
            <div class="hg-brand-avatar-wrap">
              <img src="harshlogo.png" alt="HarshGuruJi Logo" class="hg-brand-logo" />
            </div>
            <span class="hg-brand-name-text">HarshGuruJi</span>
          </div>
          <p>HarshGuruJi is a premium digital platform for learning, AI, tools, knowledge, games, and useful online resources. Learn • Create • Explore • Build.</p>
          
          <div class="hg-footer-search">
            <input type="text" id="hg-footer-search-input" placeholder="Search HarshGuruJi..." aria-label="Search the website">
            <button type="button" id="hg-footer-search-btn" aria-label="Search">Search</button>
          </div>

          <div class="hg-footer-socials">
            <a href="https://youtube.com/@harshguruji1" target="_blank" rel="noopener noreferrer" aria-label="YouTube" title="HarshGuruJi YouTube Channel">
              <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20"><path d="M23.5 6.2a3 3 0 00-2.1-2.1C19.5 3.6 12 3.6 12 3.6s-7.5 0-9.4.5A3 3 0 00.5 6.2C0 8.1 0 12 0 12s0 3.9.5 5.8a3 3 0 002.1 2.1c1.9.5 9.4.5 9.4.5s7.5 0 9.4-.5a3 3 0 002.1-2.1c.5-1.9.5-5.8.5-5.8s0-3.9-.5-5.8zM9.6 15.6V8.4l6.3 3.6-6.3 3.6z" /></svg>
            </a>
            <a href="https://www.instagram.com/harshguruji1/" target="_blank" rel="noopener noreferrer" aria-label="Instagram" title="HarshGuruJi Instagram Profile">
              <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20"><path d="M12 2.2c3.2 0 3.6 0 4.9.1 3.3.1 4.8 1.7 4.9 4.9.1 1.3.1 1.6.1 4.8 0 3.2 0 3.6-.1 4.8-.1 3.2-1.7 4.8-4.9 4.9-1.3.1-1.6.1-4.9.1-3.2 0-3.6 0-4.8-.1-3.3-.1-4.8-1.7-4.9-4.9C2.2 15.6 2.2 15.2 2.2 12c0-3.2 0-3.6.1-4.8C2.4 3.9 4 2.3 7.2 2.3c1.3-.1 1.6-.1 4.8-.1zm0-2.2C8.7 0 8.3 0 7.1.1 2.7.3.3 2.7.1 7.1 0 8.3 0 8.7 0 12c0 3.3 0 3.7.1 4.9.2 4.4 2.6 6.8 7 7C8.3 24 8.7 24 12 24c3.3 0 3.7 0 4.9-.1 4.4-.2 6.8-2.6 7-7 .1-1.2.1-1.6.1-4.9 0-3.3 0-3.7-.1-4.9C23.7 2.7 21.3.3 16.9.1 15.7 0 15.3 0 12 0zm0 5.8a6.2 6.2 0 100 12.4A6.2 6.2 0 0012 5.8zm0 10.2a4 4 0 110-8 4 4 0 010 8zm6.4-11.8a1.4 1.4 0 100 2.8 1.4 1.4 0 000-2.8z" /></svg>
            </a>
            <a href="https://t.me/officialharshguruji1" id="footer-social-telegram" target="_blank" rel="noopener noreferrer" aria-label="Telegram" title="HarshGuruJi Official Telegram Channel">
              <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm4.64 6.8c-.15 1.58-.8 5.42-1.13 7.19-.14.75-.42 1-.68 1.03-.58.05-1.02-.38-1.58-.75-.88-.58-1.38-.94-2.23-1.5-.99-.65-.35-1.01.22-1.59.15-.15 2.71-2.48 2.76-2.69a.2.2 0 00-.05-.18c-.06-.05-.14-.03-.21-.02-.09.02-1.49.95-4.22 2.79-.4.27-.76.41-1.08.4-.36-.01-1.04-.2-1.55-.37-.63-.2-1.12-.31-1.08-.66.02-.18.27-.36.74-.55 2.92-1.27 4.86-2.11 5.83-2.51 2.78-1.16 3.35-1.36 3.73-1.36.08 0 .27.02.39.12.1.08.13.19.14.27-.01.06.01.24 0 .38z"/></svg>
            </a>
          </div>
        </div>

        <nav class="hg-footer-navs" aria-label="Footer Navigation">
          <div class="hg-footer-col">
            <h5>
              <span class="hg-col-badge-icon">
                <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="M2 12h20M12 2a15.3 15.3 0 014 10 15.3 15.3 0 01-4 10 15.3 15.3 0 01-4-10 15.3 15.3 0 014-10z"/></svg>
              </span>
              <span>Platform</span>
            </h5>
            <ul>
              <li><a href="${MAIN_SITE}/index.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>HarshGuruJi Home</span></a></li>
              <li><a href="${MAIN_SITE}/ai-hub.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>AI Hub</span><span class="hg-pill-badge hg-pill-purple">AI</span></a></li>
              <li><a href="${MAIN_SITE}/daily-special.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Daily Special</span></a></li>
              <li><a href="${MAIN_SITE}/dashboard.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Dashboard</span></a></li>
              <li><a href="${MAIN_SITE}/explore.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Explore</span></a></li>
            </ul>
          </div>
          <div class="hg-footer-col">
            <h5>
              <span class="hg-col-badge-icon">
                <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><path d="M14.7 6.3a1 1 0 000 1.4l1.6 1.6a1 1 0 001.4 0l3.77-3.77a6 6 0 01-7.94 7.94l-6.91 6.91a2.12 2.12 0 01-3-3l6.91-6.91a6 6 0 017.94-7.94l-3.76 3.76z"/></svg>
              </span>
              <span>Tools</span>
            </h5>
            <ul>
              <li><a href="${MAIN_SITE}/free-tools.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>All Tools</span><span class="hg-pill-badge hg-pill-blue">50+</span></a></li>
              <li><a href="${MAIN_SITE}/explore.html?search=Text" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Text Tools</span></a></li>
              <li><a href="${MAIN_SITE}/explore.html?search=Calculator" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Calculators</span></a></li>
              <li><a href="${MAIN_SITE}/explore.html?search=Converter" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Converters</span></a></li>
              <li><a href="${MAIN_SITE}/explore.html?search=Developer" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Dev Tools</span></a></li>
            </ul>
          </div>
          <div class="hg-footer-col">
            <h5>
              <span class="hg-col-badge-icon">
                <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 19.5A2.5 2.5 0 016.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 014 19.5v-15A2.5 2.5 0 016.5 2z"/></svg>
              </span>
              <span>Learning</span>
            </h5>
            <ul>
              <li><a href="${MAIN_SITE}/learning-hub.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Learning Hub</span></a></li>
              <li><a href="https://books.webguruji.online" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>NCERT Books Library</span></a></li>
              <li><a href="${MAIN_SITE}/education.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Education</span></a></li>
              <li><a href="${MAIN_SITE}/learning/class-10-science.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Class 10</span></a></li>
              <li><a href="${MAIN_SITE}/learning/class.html?class=9" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Class 9</span></a></li>
              <li><a href="${MAIN_SITE}/learning/gk-quiz.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Quizzes</span></a></li>
            </ul>
          </div>
          <div class="hg-footer-col">
            <h5>
              <span class="hg-col-badge-icon">
                <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="6" width="20" height="12" rx="2"/><path d="M6 12h4M8 10v4M15 11h.01M18 13h.01"/></svg>
              </span>
              <span>Games & Apps</span>
            </h5>
            <ul>
              <li><a href="https://store.webguruji.online" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>HarshGuruJi Store</span><span class="hg-pill-badge hg-pill-emerald">Store</span></a></li>
              <li><a href="https://chat.webguruji.online" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Chat HarshGuruJi</span><span class="hg-pill-badge hg-pill-amber">GPT</span></a></li>
              <li><a href="https://tube.webguruji.online/" class="hg-link-highlight"><span class="hg-nav-bullet"></span><span>GuruTube</span><span class="hg-pill-badge hg-pill-cyan">This App</span></a></li>
              <li><a href="https://github.com/harshguruji01/GuruTube" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>GuruTube GitHub</span></a></li>
              <li><a href="${MAIN_SITE}/gaming-hub.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Gaming Hub</span></a></li>
              <li><a href="${MAIN_SITE}/Quiz India/index.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Quiz India</span></a></li>
            </ul>
          </div>
          <div class="hg-footer-col">
            <h5>
              <span class="hg-col-badge-icon">
                <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
              </span>
              <span>Legal & Info</span>
            </h5>
            <ul>
              <li><a href="${MAIN_SITE}/about.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>About Us</span></a></li>
              <li><a href="${MAIN_SITE}/contact.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Contact Us</span></a></li>
              <li><a href="${MAIN_SITE}/contributor.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Contributors</span></a></li>
              <li><a href="${MAIN_SITE}/privacy-policy.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Privacy Policy</span></a></li>
              <li><a href="${MAIN_SITE}/terms-and-conditions.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Terms</span></a></li>
              <li><a href="https://github.com/harshguruji01/GuruTube/blob/main/LICENSE" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>GNU GPL-3.0 License</span></a></li>
              <li><a href="${MAIN_SITE}/cookie.html" target="_blank" rel="noopener noreferrer"><span class="hg-nav-bullet"></span><span>Cookies</span></a></li>
            </ul>
          </div>
        </nav>
      </div>

      <div class="hg-footer-bottom">
        <a href="${MAIN_SITE}/owner.html" target="_blank" rel="noopener noreferrer" class="hg-founder-link" title="Meet the Owner: Harsh Patel" aria-label="Harsh Patel Founder Profile">
          <div class="hg-founder-avatar-wrap">
            <img src="harshlogo.png" loading="lazy" alt="Harsh Patel – Founder" class="hg-founder-avatar" />
          </div>
          <div class="hg-founder-info">
            <span class="hg-founder-name-animated">Harsh Patel <span class="hg-golden-tick" title="Verified Creator & Founder">✓</span></span>
            <span class="hg-founder-badge">Founder &amp; Owner</span>
          </div>
        </a>
        
        <p style="color: var(--text-secondary, #a1a1aa); font-size: 0.85rem; margin: 0;">
          &copy; <span id="hg-footer-year">${currentYear}</span> HarshGuruJi • GuruTube Project. All Rights Reserved. <br>
          Made with ❤️ by HarshGuruJi.
        </p>
      </div>
    </footer>
  `;

  if (oldFooterPlaceholder) {
    oldFooterPlaceholder.outerHTML = footerHTML;
  } else {
    document.body.insertAdjacentHTML('beforeend', footerHTML);
  }

  // Footer Search logic
  const searchBtn = document.getElementById('hg-footer-search-btn');
  const searchInput = document.getElementById('hg-footer-search-input');
  
  if (searchBtn && searchInput) {
    searchBtn.addEventListener('click', function() {
      const val = searchInput.value.trim();
      if(val) {
        window.open(`${MAIN_SITE}/explore.html?search=${encodeURIComponent(val)}`, '_blank');
      }
    });
    
    searchInput.addEventListener('keypress', function(e) {
      if(e.key === 'Enter') {
        const val = this.value.trim();
        if(val) {
          window.open(`${MAIN_SITE}/explore.html?search=${encodeURIComponent(val)}`, '_blank');
        }
      }
    });
  }
});

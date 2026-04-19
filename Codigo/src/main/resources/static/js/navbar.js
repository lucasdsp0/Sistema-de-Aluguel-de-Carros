// Navbar Interactive Effects - Premium Version
document.addEventListener('DOMContentLoaded', function() {
    const navbar = document.querySelector('.navbar');
    const navbarContainer = document.querySelector('.navbar-container');
    let lastScrollTop = 0;
    let isScrolling = false;

    // Smooth hide/show on scroll with debounce
    const scrollDebounce = function() {
        if (isScrolling) return;
        isScrolling = true;

        const currentScroll = window.pageYOffset || document.documentElement.scrollTop;

        if (currentScroll > lastScrollTop && currentScroll > 100) {
            // Scrolling down - hide
            navbar.style.transform = 'translateY(-100%)';
            navbar.style.opacity = '0';
        } else {
            // Scrolling up - show
            navbar.style.transform = 'translateY(0)';
            navbar.style.opacity = '1';
        }

        lastScrollTop = currentScroll <= 0 ? 0 : currentScroll;

        setTimeout(() => {
            isScrolling = false;
        }, 100);
    };

    window.addEventListener('scroll', scrollDebounce, { passive: true });

    // Smooth transitions
    navbar.style.transition = 'all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1)';
    navbar.style.opacity = '1';

    // Active link highlighting with smooth animation
    const currentPath = window.location.pathname;
    document.querySelectorAll('.navbar-menu a').forEach(link => {
        const href = link.getAttribute('href');
        if (currentPath.includes(href) && href !== '/auth/logout') {
            link.style.background = 'rgba(255, 255, 255, 0.25)';
            link.style.borderColor = 'rgba(255, 255, 255, 0.5)';
            link.style.fontWeight = '600';
            link.style.boxShadow = '0 8px 16px rgba(0, 0, 0, 0.15)';
        }
    });

    // Ripple effect on click
    document.querySelectorAll('.navbar-menu a').forEach(link => {
        link.addEventListener('click', function(e) {
            const ripple = document.createElement('span');
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = e.clientX - rect.left - size / 2;
            const y = e.clientY - rect.top - size / 2;

            ripple.style.width = ripple.style.height = size + 'px';
            ripple.style.left = x + 'px';
            ripple.style.top = y + 'px';
            ripple.style.position = 'absolute';
            ripple.style.borderRadius = '50%';
            ripple.style.background = 'rgba(255, 255, 255, 0.6)';
            ripple.style.pointerEvents = 'none';
            ripple.style.animation = 'ripple 0.6s ease-out';
            ripple.style.opacity = '0';

            const style = document.createElement('style');
            if (!document.querySelector('style[data-ripple]')) {
                style.setAttribute('data-ripple', 'true');
                style.innerHTML = `
                    @keyframes ripple {
                        to {
                            transform: scale(4);
                            opacity: 0;
                        }
                    }
                `;
                document.head.appendChild(style);
            }

            this.style.position = 'relative';
            this.style.overflow = 'hidden';
            this.appendChild(ripple);

            setTimeout(() => ripple.remove(), 600);
        });
    });

    // Mobile menu touch optimization
    if (window.innerWidth <= 768) {
        document.querySelectorAll('.navbar-menu a').forEach(link => {
            link.addEventListener('touchstart', function() {
                this.style.background = 'rgba(255, 255, 255, 0.3)';
                this.style.transform = 'scale(0.98)';
            });

            link.addEventListener('touchend', function() {
                setTimeout(() => {
                    this.style.background = '';
                    this.style.transform = '';
                }, 150);
            });
        });
    }

    // Parallax effect on brand
    const brand = document.querySelector('.navbar-brand');
    window.addEventListener('mousemove', function(e) {
        if (window.innerWidth > 768 && brand) {
            const x = (e.clientX / window.innerWidth) * 5 - 2.5;
            const y = (e.clientY / window.innerHeight) * 5 - 2.5;
            brand.style.transform = `perspective(1000px) rotateY(${x * 0.5}deg) rotateX(${y * 0.5}deg)`;
        }
    });
});

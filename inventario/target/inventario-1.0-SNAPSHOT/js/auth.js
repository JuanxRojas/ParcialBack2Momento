function iniciarSesion() {
    const usuario = document.getElementById('usuario').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!usuario || !password) {
        mostrarError('Por favor completa todos los campos');
        return;
    }

    // Login simple para el parcial
    if (usuario === 'admin' && password === 'admin') {
        sessionStorage.setItem('usuario', usuario);
        window.location.href = 'dashboard.html';
    } else {
        mostrarError('Usuario o contraseña incorrectos');
    }
}

function mostrarError(msg) {
    const el = document.getElementById('errorMsg');
    el.textContent = msg;
    el.style.display = 'block';
}

// Permitir login con Enter
document.addEventListener('keypress', function(e) {
    if (e.key === 'Enter') iniciarSesion();
});
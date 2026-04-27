const BASE = '/inventario/api';

// UTILIDADES
function mostrarTab(nombre) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    document.getElementById('tab-' + nombre).classList.add('active');
    event.target.classList.add('active');

    if (nombre === 'proveedores') cargarProveedores();
    if (nombre === 'productos')   cargarProductos();
    if (nombre === 'clientes')    cargarClientes();
    if (nombre === 'pedidos')     cargarPedidos();
    if (nombre === 'historial')   cargarHistorialTodos();
}

function abrirModal(id)  { document.getElementById(id).classList.add('active'); }
function cerrarModal(id) { document.getElementById(id).classList.remove('active'); }

function cerrarSesion() {
    sessionStorage.clear();
    window.location.href = 'index.html';
}

function badgeEstado(estado) {
    const clases = {
        'pendiente':  'badge badge-pendiente',
        'en proceso': 'badge badge-proceso',
        'entregado':  'badge badge-entregado'
    };
    return `<span class="${clases[estado] || 'badge'}">${estado}</span>`;
}

// PROVEEDORES
async function cargarProveedores() {
    const tbody = document.getElementById('tablaProveedores');
    try {
        const resp = await fetch(`${BASE}/proveedores`);
        console.log('Status:', resp.status);
        console.log('URL consultada:', `${BASE}/proveedores`);

        if (!resp.ok) {
            tbody.innerHTML = `<tr><td colspan="6" class="empty-msg">
                Error ${resp.status} - URL: ${BASE}/proveedores
            </td></tr>`;
            return;
        }

        const data = await resp.json();
        console.log('Datos recibidos:', data);

        if (!data.length) {
            tbody.innerHTML = '<tr><td colspan="6" class="empty-msg">No hay proveedores registrados</td></tr>';
            return;
        }

        tbody.innerHTML = data.map(p => `
            <tr>
                <td>${p.idProveedor}</td>
                <td>${p.nombre}</td>
                <td>${p.contacto || '-'}</td>
                <td>${p.telefono || '-'}</td>
                <td>${p.email || '-'}</td>
                <td>
                    <button class="btn-edit" onclick="editarProveedor(${p.idProveedor})">️ Editar</button>
                    <button class="btn-delete" onclick="eliminarProveedor(${p.idProveedor})">️ Eliminar</button>
                </td>
            </tr>`).join('');

    } catch (e) {
        tbody.innerHTML = `<tr><td colspan="6" class="empty-msg">
           Error de conexión: ${e.message}
        </td></tr>`;
        console.error('Error:', e);
    }
}

function abrirModalProveedor() {
    document.getElementById('tituloModalProveedor').textContent = 'Nuevo Proveedor';
    document.getElementById('proveedorId').value = '';
    document.getElementById('proveedorNombre').value = '';
    document.getElementById('proveedorContacto').value = '';
    document.getElementById('proveedorTelefono').value = '';
    document.getElementById('proveedorEmail').value = '';
    abrirModal('modalProveedor');
}

async function editarProveedor(id) {
    const resp = await fetch(`${BASE}/proveedores/${id}`);
    const p = await resp.json();
    document.getElementById('tituloModalProveedor').textContent = 'Editar Proveedor';
    document.getElementById('proveedorId').value      = p.idProveedor;
    document.getElementById('proveedorNombre').value  = p.nombre;
    document.getElementById('proveedorContacto').value = p.contacto || '';
    document.getElementById('proveedorTelefono').value = p.telefono || '';
    document.getElementById('proveedorEmail').value   = p.email || '';
    abrirModal('modalProveedor');
}

async function guardarProveedor() {
    const id = document.getElementById('proveedorId').value;
    const body = {
        nombre:   document.getElementById('proveedorNombre').value,
        contacto: document.getElementById('proveedorContacto').value,
        telefono: document.getElementById('proveedorTelefono').value,
        email:    document.getElementById('proveedorEmail').value
    };

    const url    = id ? `${BASE}/proveedores/${id}` : `${BASE}/proveedores`;
    const method = id ? 'PUT' : 'POST';

    await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });

    cerrarModal('modalProveedor');
    cargarProveedores();
}

async function eliminarProveedor(id) {
    if (!confirm('¿Eliminar este proveedor?')) return;
    await fetch(`${BASE}/proveedores/${id}`, { method: 'DELETE' });
    cargarProveedores();
}

// PRODUCTOS
async function cargarProductos() {
    const resp = await fetch(`${BASE}/productos`);
    const data = await resp.json();
    const tbody = document.getElementById('tablaProductos');

    if (!data.length) {
        tbody.innerHTML = '<tr><td colspan="7" class="empty-msg">No hay productos registrados</td></tr>';
        return;
    }

    tbody.innerHTML = data.map(p => `
        <tr>
            <td>${p.idProducto}</td>
            <td>${p.nombre}</td>
            <td>${p.descripcion || '-'}</td>
            <td>$${Number(p.precio).toLocaleString('es-CO')}</td>
            <td>${p.stock}</td>
            <td>${p.proveedor ? p.proveedor.nombre : '-'}</td>
            <td>
                <button class="btn-edit" onclick="editarProducto(${p.idProducto})">️ Editar</button>
                <button class="btn-delete" onclick="eliminarProducto(${p.idProducto})"> Eliminar</button>
            </td>
        </tr>`).join('');
}

async function abrirModalProducto() {
    document.getElementById('tituloModalProducto').textContent = 'Nuevo Producto';
    document.getElementById('productoId').value = '';
    document.getElementById('productoNombre').value = '';
    document.getElementById('productoDescripcion').value = '';
    document.getElementById('productoPrecio').value = '';
    document.getElementById('productoStock').value = '';
    await cargarSelectProveedores('productoProveedor');
    abrirModal('modalProducto');
}

async function editarProducto(id) {
    const resp = await fetch(`${BASE}/productos/${id}`);
    const p = await resp.json();
    document.getElementById('tituloModalProducto').textContent = 'Editar Producto';
    document.getElementById('productoId').value          = p.idProducto;
    document.getElementById('productoNombre').value      = p.nombre;
    document.getElementById('productoDescripcion').value = p.descripcion || '';
    document.getElementById('productoPrecio').value      = p.precio;
    document.getElementById('productoStock').value       = p.stock;
    await cargarSelectProveedores('productoProveedor', p.proveedor?.idProveedor);
    abrirModal('modalProducto');
}

async function guardarProducto() {
    const id = document.getElementById('productoId').value;
    const body = {
        nombre:      document.getElementById('productoNombre').value,
        descripcion: document.getElementById('productoDescripcion').value,
        precio:      document.getElementById('productoPrecio').value,
        stock:       parseInt(document.getElementById('productoStock').value),
        idProveedor: parseInt(document.getElementById('productoProveedor').value)
    };

    const url    = id ? `${BASE}/productos/${id}` : `${BASE}/productos`;
    const method = id ? 'PUT' : 'POST';

    await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });

    cerrarModal('modalProducto');
    cargarProductos();
}

async function eliminarProducto(id) {
    if (!confirm('¿Eliminar este producto?')) return;
    await fetch(`${BASE}/productos/${id}`, { method: 'DELETE' });
    cargarProductos();
}

// CLIENTES
async function cargarClientes() {
    const resp = await fetch(`${BASE}/clientes`);
    const data = await resp.json();
    const tbody = document.getElementById('tablaClientes');

    if (!data.length) {
        tbody.innerHTML = '<tr><td colspan="6" class="empty-msg">No hay clientes registrados</td></tr>';
        return;
    }

    tbody.innerHTML = data.map(c => `
        <tr>
            <td>${c.idCliente}</td>
            <td>${c.nombre}</td>
            <td>${c.direccion || '-'}</td>
            <td>${c.telefono || '-'}</td>
            <td>${c.email || '-'}</td>
            <td>
                <button class="btn-edit" onclick="editarCliente(${c.idCliente})">️ Editar</button>
                <button class="btn-delete" onclick="eliminarCliente(${c.idCliente})"> Eliminar</button>
            </td>
        </tr>`).join('');
}

function abrirModalCliente() {
    document.getElementById('tituloModalCliente').textContent = 'Nuevo Cliente';
    document.getElementById('clienteId').value = '';
    document.getElementById('clienteNombre').value = '';
    document.getElementById('clienteDireccion').value = '';
    document.getElementById('clienteTelefono').value = '';
    document.getElementById('clienteEmail').value = '';
    abrirModal('modalCliente');
}

async function editarCliente(id) {
    const resp = await fetch(`${BASE}/clientes/${id}`);
    const c = await resp.json();
    document.getElementById('tituloModalCliente').textContent = 'Editar Cliente';
    document.getElementById('clienteId').value        = c.idCliente;
    document.getElementById('clienteNombre').value    = c.nombre;
    document.getElementById('clienteDireccion').value = c.direccion || '';
    document.getElementById('clienteTelefono').value  = c.telefono || '';
    document.getElementById('clienteEmail').value     = c.email || '';
    abrirModal('modalCliente');
}

async function guardarCliente() {
    const id = document.getElementById('clienteId').value;
    const body = {
        nombre:    document.getElementById('clienteNombre').value,
        direccion: document.getElementById('clienteDireccion').value,
        telefono:  document.getElementById('clienteTelefono').value,
        email:     document.getElementById('clienteEmail').value
    };

    const url    = id ? `${BASE}/clientes/${id}` : `${BASE}/clientes`;
    const method = id ? 'PUT' : 'POST';

    await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });

    cerrarModal('modalCliente');
    cargarClientes();
}

async function eliminarCliente(id) {
    if (!confirm('¿Eliminar este cliente?')) return;
    await fetch(`${BASE}/clientes/${id}`, { method: 'DELETE' });
    cargarClientes();
}
// PEDIDOS
async function cargarPedidos() {
    const resp = await fetch(`${BASE}/pedidos`);
    const data = await resp.json();
    const tbody = document.getElementById('tablaPedidos');

    if (!data.length) {
        tbody.innerHTML = '<tr><td colspan="5" class="empty-msg">No hay pedidos registrados</td></tr>';
        return;
    }

    tbody.innerHTML = data.map(p => `
        <tr>
            <td>${p.idPedido}</td>
            <td>${p.cliente ? p.cliente.nombre : '-'}</td>
            <td>${p.fechaPedido || '-'}</td>
            <td>${badgeEstado(p.estado)}</td>
            <td>
                <button class="btn-edit" onclick="cambiarEstadoPedido(${p.idPedido}, '${p.estado}')">
                   Estado
                </button>
            </td>
        </tr>`).join('');
}

async function abrirModalPedido() {
    await cargarSelectClientes('pedidoCliente');
    await cargarSelectsDetalles();
    abrirModal('modalPedido');
}

async function guardarPedido() {
    const idCliente = parseInt(document.getElementById('pedidoCliente').value);
    const filas = document.querySelectorAll('#detallesPedido .detalle-item');

    const detalles = [];
    for (const fila of filas) {
        const idProducto = parseInt(fila.querySelector('.productoSelect').value);
        const cantidad   = parseInt(fila.querySelector('.cantidadInput').value);
        if (idProducto && cantidad > 0) {
            detalles.push({ idProducto, cantidad });
        }
    }

    if (!detalles.length) {
        alert('Agrega al menos un producto al pedido');
        return;
    }

    const resp = await fetch(`${BASE}/pedidos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ idCliente, detalles })
    });

    if (resp.ok) {
        cerrarModal('modalPedido');
        cargarPedidos();
        alert('✅ Pedido creado y stock actualizado correctamente');
    } else {
        const err = await resp.json();
        alert('Error: ' + err.error);
    }
}

async function cambiarEstadoPedido(id, estadoActual) {
    const estados = ['pendiente', 'en proceso', 'entregado'];
    const siguiente = estados[(estados.indexOf(estadoActual) + 1) % estados.length];
    if (!confirm(`¿Cambiar estado a "${siguiente}"?`)) return;

    await fetch(`${BASE}/pedidos/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ estado: siguiente })
    });

    cargarPedidos();
}

// HISTORIAL
async function cargarHistorialTodos() {
    const resp = await fetch(`${BASE}/historial`);
    const data = await resp.json();
    renderHistorial(data);
}

async function buscarHistorial() {
    const idCliente = document.getElementById('inputClienteHistorial').value;
    if (!idCliente) { cargarHistorialTodos(); return; }

    const resp = await fetch(`${BASE}/historial/cliente/${idCliente}`);
    const data = await resp.json();
    renderHistorial(data);
}

function renderHistorial(data) {
    const tbody = document.getElementById('tablaHistorial');

    if (!data.length) {
        tbody.innerHTML = '<tr><td colspan="4" class="empty-msg">No hay entregas registradas</td></tr>';
        return;
    }

    tbody.innerHTML = data.map(h => `
        <tr>
            <td>${h.idEntrega}</td>
            <td>${h.pedido ? h.pedido.idPedido : '-'}</td>
            <td>${h.fechaEntrega || '-'}</td>
            <td>${badgeEstado(h.estadoEntrega)}</td>
        </tr>`).join('');
}

// HELPERS - Cargar selects
async function cargarSelectProveedores(selectId, selectedId = null) {
    const resp = await fetch(`${BASE}/proveedores`);
    const data = await resp.json();
    const select = document.getElementById(selectId);
    select.innerHTML = data.map(p =>
        `<option value="${p.idProveedor}" ${p.idProveedor == selectedId ? 'selected' : ''}>
            ${p.nombre}
        </option>`
    ).join('');
}

async function cargarSelectClientes(selectId) {
    const resp = await fetch(`${BASE}/clientes`);
    const data = await resp.json();
    const select = document.getElementById(selectId);
    select.innerHTML = data.map(c =>
        `<option value="${c.idCliente}">${c.nombre}</option>`
    ).join('');
}

async function cargarSelectsDetalles() {
    const resp = await fetch(`${BASE}/productos`);
    const productos = await resp.json();
    const contenedor = document.getElementById('detallesPedido');
    contenedor.innerHTML = `
        <div class="detalle-item">
            <select class="productoSelect">
                ${productos.map(p => `<option value="${p.idProducto}">${p.nombre} (stock: ${p.stock})</option>`).join('')}
            </select>
            <input type="number" min="1" placeholder="Cant." class="cantidadInput"/>
        </div>`;
    // Guardamos productos para usarlos al agregar más filas
    window._productos = productos;
}

function agregarDetalle() {
    const productos = window._productos || [];
    const div = document.createElement('div');
    div.className = 'detalle-item';
    div.innerHTML = `
        <select class="productoSelect">
            ${productos.map(p => `<option value="${p.idProducto}">${p.nombre} (stock: ${p.stock})</option>`).join('')}
        </select>
        <input type="number" min="1" placeholder="Cant." class="cantidadInput"/>
        <button onclick="this.parentElement.remove()" style="background:#fed7d7;border:none;border-radius:5px;padding:5px 10px;cursor:pointer;">✕</button>`;
    document.getElementById('detallesPedido').appendChild(div);
}

// INIT
window.onload = function () {
    if (!sessionStorage.getItem('usuario')) {
        window.location.href = 'index.html';
        return;
    }
    cargarProveedores();
};
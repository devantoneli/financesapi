// Base URL do servidor
const baseUrl = 'http://localhost:7078';

// Helper para fetch com token
function fetchWithToken(url, options = {}, token = null) {
    const headers = { 'Content-Type': 'application/json' };
    if (token) headers['Authorization'] = token;
    return fetch(url, { ...options, headers });
}

// Helper para parse response (sempre JSON agora)
async function parseResponse(response) {
    return await response.json();
}

// Helper para log
function log(msg) { console.log(msg); }

// Testes
async function runTests() {
    log('Iniciando testes API...');
    
    // 1. Login admin (password hasheada, mas envia plain)
    log('1. Login admin...');
    let response = await fetchWithToken(`${baseUrl}/login`, {
        method: 'POST',
        body: JSON.stringify({ username: 'admin', password: 'admin123' })
    });
    let data = await parseResponse(response);
    log(`Login admin: ${response.status} - ${JSON.stringify(data)}`);
    const adminToken = response.ok ? data.token : null;
    
    // 2. Logout admin (se logado)
    if (adminToken) {
        log('2. Logout admin...');
        response = await fetchWithToken(`${baseUrl}/logout`, {
            method: 'PUT'
        }, adminToken);
        data = await parseResponse(response);
        log(`Logout admin: ${response.status} - ${JSON.stringify(data)}`);
    }
    
    // 3. Login de novo para continuar testes
    log('3. Login admin novamente...');
    response = await fetchWithToken(`${baseUrl}/login`, {
        method: 'POST',
        body: JSON.stringify({ username: 'admin', password: 'admin123' })
    });
    data = await parseResponse(response);
    log(`Login admin: ${response.status} - ${JSON.stringify(data)}`);
    const newToken = response.ok ? data.token : null;
    
    if (!newToken) {
        log('Falha no login, abortando.');
        return;
    }
    
    // 4. Criar user admin (via admin)
    log('4. Criar user teste...');
    response = await fetchWithToken(`${baseUrl}/api/admin/users`, {
        method: 'POST',
        body: JSON.stringify({ username: 'teste', password: '123456', role: 'USER' })
    }, newToken);
    data = await parseResponse(response);
    log(`Criar user: ${response.status} - ${JSON.stringify(data)}`);
    
    // 5. Listar users admin
    log('5. Listar users...');
    response = await fetchWithToken(`${baseUrl}/api/admin/users`, {}, newToken);
    data = await parseResponse(response);
    log(`Listar users: ${response.status} - ${JSON.stringify(data)}`);
    
    // 6. Login como user teste
    log('6. Login user teste...');
    response = await fetchWithToken(`${baseUrl}/login`, {
        method: 'POST',
        body: JSON.stringify({ username: 'teste', password: '123456' })
    });
    data = await parseResponse(response);
    log(`Login teste: ${response.status} - ${JSON.stringify(data)}`);
    const userToken = response.ok ? data.token : null;
    
    if (!userToken) {
        log('Falha no login user, abortando.');
        return;
    }
    
    // 7. Criar task para user
    log('7. Criar task...');
    response = await fetchWithToken(`${baseUrl}/api/tasks`, {
        method: 'POST',
        body: JSON.stringify({ title: 'Tarefa de teste' })
    }, userToken);
    data = await parseResponse(response);
    log(`Criar task: ${response.status} - ${JSON.stringify(data)}`);
    
    // 8. Listar tasks do user
    log('8. Listar tasks...');
    response = await fetchWithToken(`${baseUrl}/api/tasks`, {}, userToken);
    data = await parseResponse(response);
    log(`Listar tasks: ${response.status} - ${JSON.stringify(data)}`);
    const taskId = data.length > 0 ? data[0].id : null;
    
    if (taskId) {
        // 9. Atualizar task
        log('9. Atualizar task...');
        response = await fetchWithToken(`${baseUrl}/api/tasks/${taskId}`, {
            method: 'PUT',
            body: JSON.stringify({ title: 'Tarefa atualizada', done: true })
        }, userToken);
        data = await parseResponse(response);
        log(`Atualizar task: ${response.status} - ${JSON.stringify(data)}`);
        
        // 10. Deletar task
        log('10. Deletar task...');
        response = await fetchWithToken(`${baseUrl}/api/tasks/${taskId}`, {
            method: 'DELETE'
        }, userToken);
        data = await parseResponse(response);
        log(`Deletar task: ${response.status} - ${JSON.stringify(data)}`);
    }
    
    // 11. Logout user
    log('11. Logout user...');
    response = await fetchWithToken(`${baseUrl}/logout`, {
        method: 'PUT'
    }, userToken);
    data = await parseResponse(response);
    log(`Logout user: ${response.status} - ${JSON.stringify(data)}`);
    
    // 12. Deletar user teste (via admin)
    log('12. Deletar user teste...');
    response = await fetchWithToken(`${baseUrl}/api/admin/users/teste`, {
        method: 'DELETE'
    }, newToken);
    data = await parseResponse(response);
    log(`Deletar user: ${response.status} - ${JSON.stringify(data)}`);
    
    // 13. Logout admin final
    log('13. Logout admin final...');
    response = await fetchWithToken(`${baseUrl}/logout`, {
        method: 'PUT'
    }, newToken);
    data = await parseResponse(response);
    log(`Logout admin: ${response.status} - ${JSON.stringify(data)}`);
    
    log('Testes finalizados.');
}

// Executar
runTests().catch(err => log('Erro nos testes: ' + err));
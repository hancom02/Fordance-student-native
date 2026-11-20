const http = require('http');

async function runTests() {
  // Compile first to ensure dist/ exists
  const { execSync } = require('child_process');
  console.log('Compiling backend TypeScript...');
  execSync('npx tsc', { cwd: __dirname + '/..' });

  const app = require('../dist/server').default;
  const server = app.listen(0);
  const port = server.address().port;
  const baseUrl = `http://localhost:${port}`;
  console.log(`Test server running at ${baseUrl}`);

  function fetchUrl(path, options = {}) {
    return new Promise((resolve, reject) => {
      const url = new URL(path, baseUrl);
      const req = http.request(
        url,
        {
          method: options.method || 'GET',
          headers: {
            'Content-Type': 'application/json',
            ...(options.headers || {}),
          },
        },
        (res) => {
          let data = '';
          res.on('data', (chunk) => (data += chunk));
          res.on('end', () => {
            try {
              resolve({ status: res.statusCode, data: JSON.parse(data) });
            } catch (e) {
              resolve({ status: res.statusCode, raw: data });
            }
          });
        }
      );
      req.on('error', reject);
      if (options.body) {
        req.write(JSON.stringify(options.body));
      }
      req.end();
    });
  }

  try {
    // 1. Health check
    console.log('Testing GET /health...');
    const health = await fetchUrl('/health');
    if (health.status !== 200 || health.data.status !== 'ok') {
      throw new Error(`Health check failed: ${JSON.stringify(health)}`);
    }
    console.log('✓ Health check passed');

    // 2. Discover feeds
    console.log('Testing GET /api/v1/discover/feeds...');
    const feeds = await fetchUrl('/api/v1/discover/feeds');
    if (feeds.status !== 200 || !feeds.data.success || !Array.isArray(feeds.data.data.featured)) {
      throw new Error(`Discover feeds failed: ${JSON.stringify(feeds)}`);
    }
    console.log(`✓ Feeds passed: ${feeds.data.data.featured.length} featured sessions, ${feeds.data.data.classes.length} classes`);

    // 3. Catalog sessions
    console.log('Testing GET /api/v1/catalog/sessions...');
    const catSessions = await fetchUrl('/api/v1/catalog/sessions');
    if (catSessions.status !== 200 || !catSessions.data.success || !Array.isArray(catSessions.data.data)) {
      throw new Error(`Catalog sessions failed: ${JSON.stringify(catSessions)}`);
    }
    console.log(`✓ Catalog sessions passed: ${catSessions.data.data.length} sessions`);

    // 4. Catalog classes
    console.log('Testing GET /api/v1/catalog/classes...');
    const catClasses = await fetchUrl('/api/v1/catalog/classes');
    if (catClasses.status !== 200 || !catClasses.data.success || !Array.isArray(catClasses.data.data)) {
      throw new Error(`Catalog classes failed: ${JSON.stringify(catClasses)}`);
    }
    console.log(`✓ Catalog classes passed: ${catClasses.data.data.length} classes`);

    // 5. Catalog instructors
    console.log('Testing GET /api/v1/catalog/instructors...');
    const instructors = await fetchUrl('/api/v1/catalog/instructors');
    if (instructors.status !== 200 || !instructors.data.success || !Array.isArray(instructors.data.data)) {
      throw new Error(`Catalog instructors failed: ${JSON.stringify(instructors)}`);
    }
    console.log(`✓ Catalog instructors passed: ${instructors.data.data.length} instructors`);

    // 6. Session Detail
    if (catSessions.data.data.length > 0) {
      const sampleSessionId = catSessions.data.data[0].id;
      console.log(`Testing GET /api/v1/sessions/${sampleSessionId}...`);
      const sessionDetail = await fetchUrl(`/api/v1/sessions/${sampleSessionId}`);
      if (sessionDetail.status !== 200 || !sessionDetail.data.success || !sessionDetail.data.data.id) {
        throw new Error(`Session detail failed: ${JSON.stringify(sessionDetail)}`);
      }
      console.log(`✓ Session detail passed: "${sessionDetail.data.data.session_name}"`);
    }

    // 7. Class Detail
    if (catClasses.data.data.length > 0) {
      const sampleClassId = catClasses.data.data[0].id;
      console.log(`Testing GET /api/v1/classes/${sampleClassId}...`);
      const classDetail = await fetchUrl(`/api/v1/classes/${sampleClassId}`);
      if (classDetail.status !== 200 || !classDetail.data.success || !classDetail.data.data.id) {
        throw new Error(`Class detail failed: ${JSON.stringify(classDetail)}`);
      }
      console.log(`✓ Class detail passed: "${classDetail.data.data.class_name}" with ${classDetail.data.data.total_sessions} sessions`);
    }

    // 8. Auth invalid login
    console.log('Testing POST /api/v1/auth/login with invalid credentials...');
    const invalidLogin = await fetchUrl('/api/v1/auth/login', {
      method: 'POST',
      body: { email: 'nonexistent@fordance.com', password: 'wrongpassword' },
    });
    if (invalidLogin.status !== 401 || invalidLogin.data.success !== false) {
      throw new Error(`Invalid login check failed: ${JSON.stringify(invalidLogin)}`);
    }
    console.log('✓ Auth invalid login rejected properly with 401');

    console.log('\n=============================================');
    console.log('ALL BACKEND REST API ENDPOINT TESTS PASSED!');
    console.log('=============================================\n');
    server.close();
    process.exit(0);
  } catch (error) {
    console.error('Test execution error:', error);
    server.close();
    process.exit(1);
  }
}

runTests();

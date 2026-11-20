import express, { Request, Response, NextFunction } from 'express';
import cors from 'cors';
import dotenv from 'dotenv';

import authRoutes from './routes/auth';
import discoverRoutes from './routes/discover';
import catalogRoutes from './routes/catalog';
import sessionRoutes from './routes/sessions';
import classRoutes from './routes/classes';

dotenv.config();

const app = express();
const port = process.env.PORT || 3000;

// Middlewares
app.use(cors());
app.use(express.json());

// Routes
app.use('/api/v1/auth', authRoutes);
app.use('/api/v1/discover', discoverRoutes);
app.use('/api/v1/catalog', catalogRoutes);
app.use('/api/v1/sessions', sessionRoutes);
app.use('/api/v1/classes', classRoutes);

// Health check
app.get('/health', (_req: Request, res: Response) => {
  res.json({
    status: 'ok',
    service: 'fordance-backend',
    version: '1.0.0',
    timestamp: new Date().toISOString(),
  });
});

// Central Error Handler
app.use((err: any, _req: Request, res: Response, _next: NextFunction) => {
  console.error('Unhandled server error:', err);
  res.status(500).json({
    success: false,
    message: err.message || 'Internal server error',
  });
});

if (process.env.NODE_ENV !== 'test') {
  app.listen(Number(port), '0.0.0.0', () => {
    console.log(`[FOR DANCE Backend] Server running at http://0.0.0.0:${port}`);
    console.log(`[FOR DANCE Backend] Local IP: http://192.168.1.111:${port}`);
    console.log(`[FOR DANCE Backend] Health check: http://localhost:${port}/health`);
  });
}

export default app;

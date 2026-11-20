import { Request, Response, NextFunction } from 'express';
import { supabase } from '../config/supabase';
import { User } from '@supabase/supabase-js';

export interface AuthenticatedRequest extends Request {
  user?: User;
  token?: string;
}

export const requireAuth = async (
  req: AuthenticatedRequest,
  res: Response,
  next: NextFunction
): Promise<void> => {
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    res.status(401).json({
      success: false,
      message: 'Authorization header missing or invalid format. Expected Bearer <token>.',
    });
    return;
  }

  const token = authHeader.substring(7).trim();
  try {
    const { data: { user }, error } = await supabase.auth.getUser(token);
    if (error || !user) {
      res.status(401).json({
        success: false,
        message: 'Invalid or expired token.',
        error: error?.message,
      });
      return;
    }

    req.user = user;
    req.token = token;
    next();
  } catch (err: any) {
    res.status(500).json({
      success: false,
      message: 'Failed to authenticate user.',
      error: err?.message,
    });
  }
};

export const optionalAuth = async (
  req: AuthenticatedRequest,
  _res: Response,
  next: NextFunction
): Promise<void> => {
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return next();
  }

  const token = authHeader.substring(7).trim();
  try {
    const { data: { user } } = await supabase.auth.getUser(token);
    if (user) {
      req.user = user;
      req.token = token;
    }
  } catch {
    // Graceful fallback for unauthenticated requests
  }
  next();
};

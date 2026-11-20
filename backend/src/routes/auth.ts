import { Router, Response } from 'express';
import { supabase, getSupabaseClient } from '../config/supabase';
import { requireAuth, AuthenticatedRequest } from '../middleware/auth';

const router = Router();

// POST /api/v1/auth/login
router.post('/login', async (req, res: Response) => {
  const { email, password } = req.body;
  if (!email || !password) {
    res.status(400).json({ success: false, message: 'Email and password are required.' });
    return;
  }

  try {
    const { data, error } = await supabase.auth.signInWithPassword({ email, password });
    if (error || !data.session || !data.user) {
      res.status(401).json({
        success: false,
        message: error?.message || 'Authentication failed.',
      });
      return;
    }

    // Fetch user profile from public.users table
    const { data: profiles } = await supabase
      .from('users')
      .select('*')
      .eq('id', data.user.id)
      .limit(1);

    const profile = profiles?.[0];
    const role = profile?.role || data.user.user_metadata?.role || 'student';

    if (role !== 'student') {
      await supabase.auth.signOut();
      res.status(403).json({
        success: false,
        message: 'This account is not a student account.',
      });
      return;
    }

    const studentUser = {
      id: data.user.id,
      email: data.user.email || '',
      username: profile?.username || data.user.email?.split('@')[0] || '',
      name: profile?.name || data.user.user_metadata?.full_name || '',
      phone: profile?.phone || '',
      avatar_url: profile?.avatar_url || '',
      role: 'student',
      active: profile?.active ?? true,
    };

    res.json({
      success: true,
      data: {
        token: data.session.access_token,
        refresh_token: data.session.refresh_token,
        user: studentUser,
      },
    });
  } catch (err: any) {
    res.status(500).json({ success: false, message: err.message || 'Internal server error.' });
  }
});

// POST /api/v1/auth/register
router.post('/register', async (req, res: Response) => {
  const { email, password, name, phone } = req.body;
  if (!email || !password) {
    res.status(400).json({ success: false, message: 'Email and password are required.' });
    return;
  }

  try {
    const { data, error } = await supabase.auth.signUp({
      email,
      password,
      options: {
        data: {
          full_name: name || '',
          role: 'student',
        },
      },
    });

    if (error || !data.user) {
      res.status(400).json({
        success: false,
        message: error?.message || 'Registration failed.',
      });
      return;
    }

    // Insert student record into public.users
    const newProfile = {
      id: data.user.id,
      email,
      username: email.split('@')[0],
      name: name || '',
      phone: phone || '',
      role: 'student',
      active: true,
    };

    await supabase.from('users').upsert(newProfile);

    res.status(201).json({
      success: true,
      data: {
        token: data.session?.access_token || '',
        user: newProfile,
      },
      message: 'Registration successful.',
    });
  } catch (err: any) {
    res.status(500).json({ success: false, message: err.message || 'Internal server error.' });
  }
});

// GET /api/v1/auth/me
router.get('/me', requireAuth, async (req: AuthenticatedRequest, res: Response) => {
  try {
    const userId = req.user?.id;
    if (!userId) {
      res.status(401).json({ success: false, message: 'Unauthorized' });
      return;
    }

    const { data: profiles, error } = await supabase
      .from('users')
      .select('*')
      .eq('id', userId)
      .limit(1);

    if (error || !profiles || profiles.length === 0) {
      res.status(404).json({ success: false, message: 'User profile not found.' });
      return;
    }

    res.json({
      success: true,
      data: profiles[0],
    });
  } catch (err: any) {
    res.status(500).json({ success: false, message: err.message || 'Internal server error.' });
  }
});

export default router;

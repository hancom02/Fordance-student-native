import { Router, Response } from 'express';
import { supabase, getSupabaseClient } from '../config/supabase';
import { optionalAuth, AuthenticatedRequest } from '../middleware/auth';

const router = Router();

// GET /api/v1/discover/feeds
router.get('/feeds', optionalAuth, async (req: AuthenticatedRequest, res: Response) => {
  try {
    const client = getSupabaseClient(req.token);
    const userId = req.user?.id;

    // 1. Featured Sessions
    const { data: featured, error: featuredErr } = await client
      .from('sessions')
      .select('*')
      .neq('status', 'rejected')
      .neq('status', 'waiting')
      .neq('status', 'deleted')
      .limit(10);

    if (featuredErr) throw featuredErr;

    // 2. Classes
    const { data: classes, error: classesErr } = await client
      .from('classes')
      .select('*')
      .limit(10);

    if (classesErr) throw classesErr;

    // 3. Joined Sessions (if user authenticated)
    let joined: any[] = [];
    if (userId) {
      const { data: joinPivots } = await client
        .from('users_sessions_joined')
        .select('session_id')
        .eq('user_id', userId);

      if (joinPivots && joinPivots.length > 0) {
        const sessionIds = joinPivots.map((p) => p.session_id);
        const { data: joinedSessions } = await client
          .from('sessions')
          .select('*')
          .in('id', sessionIds);
        joined = joinedSessions || [];
      }
    }

    // 4. Favourite Sessions (if user authenticated)
    let favourites: any[] = [];
    if (userId) {
      const { data: favPivots } = await client
        .from('users_sessions_favourite')
        .select('session_id')
        .eq('user_id', userId);

      if (favPivots && favPivots.length > 0) {
        const favIds = favPivots.map((p) => p.session_id);
        const { data: favSessions } = await client
          .from('sessions')
          .select('*')
          .in('id', favIds);
        favourites = favSessions || [];
      }
    }

    res.json({
      success: true,
      data: {
        featured: featured || [],
        joined,
        classes: classes || [],
        favourites,
      },
    });
  } catch (err: any) {
    res.status(500).json({ success: false, message: err.message || 'Failed to load feeds.' });
  }
});

export default router;

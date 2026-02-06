-- Update all properties with public Unsplash image URLs
UPDATE pgs SET imageUrl = 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800' WHERE id = 1 AND (imageUrl IS NULL OR imageUrl = '');
UPDATE pgs SET imageUrl = 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800' WHERE id = 2 AND (imageUrl IS NULL OR imageUrl = '');
UPDATE pgs SET imageUrl = 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800' WHERE id = 3 AND (imageUrl IS NULL OR imageUrl = '');
UPDATE pgs SET imageUrl = 'https://images.unsplash.com/photo-1513584684374-8bab748fbf90?w=800' WHERE id = 4 AND (imageUrl IS NULL OR imageUrl = '');
UPDATE pgs SET imageUrl = 'https://images.unsplash.com/photo-1484154218962-a197022b5858?w=800' WHERE (imageUrl IS NULL OR imageUrl = '') AND id > 4;

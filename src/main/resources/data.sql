-- Insert Roles with UUIDs
INSERT INTO roles (id, name, description) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'ROLE_ADMIN', 'Administrator'),
    ('550e8400-e29b-41d4-a716-446655440002', 'ROLE_LANDLORD', 'Landlord'),
    ('550e8400-e29b-41d4-a716-446655440003', 'ROLE_RENTER', 'Renter');

-- Insert Amenities with UUIDs
INSERT INTO amenities (id, name, description) VALUES
    ('660e8400-e29b-41d4-a716-446655440001', 'WiFi', 'High-speed internet'),
    ('660e8400-e29b-41d4-a716-446655440002', 'Parking', 'Dedicated parking space'),
    ('660e8400-e29b-41d4-a716-446655440003', 'Pool', 'Swimming pool'),
    ('660e8400-e29b-41d4-a716-446655440004', 'Gym', 'Fitness center'),
    ('660e8400-e29b-41d4-a716-446655440005', 'Air Conditioning', 'Central air conditioning'),
    ('660e8400-e29b-41d4-a716-446655440006', 'Washer/Dryer', 'In-unit laundry');
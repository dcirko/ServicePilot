DO $$
DECLARE
    customer_record RECORD;
    customer_role_id BIGINT;
    linked_user_id BIGINT;
    generated_email TEXT;
BEGIN
    IF to_regclass('public.customers') IS NULL THEN
        RETURN;
    END IF;

    ALTER TABLE public.customers
        ADD COLUMN IF NOT EXISTS user_id BIGINT;

    IF to_regclass('public.roles') IS NULL OR to_regclass('public.users') IS NULL THEN
        RETURN;
    END IF;

    INSERT INTO public.roles (name, created_at, updated_at)
    SELECT 'CUSTOMER', CURRENT_TIMESTAMP, NULL
    WHERE NOT EXISTS (
        SELECT 1
        FROM public.roles
        WHERE name = 'CUSTOMER'
    );

    SELECT id
    INTO customer_role_id
    FROM public.roles
    WHERE name = 'CUSTOMER';

    FOR customer_record IN
        SELECT id, first_name, last_name, email, phone
        FROM public.customers
        WHERE user_id IS NULL
        ORDER BY id
    LOOP
        SELECT id
        INTO linked_user_id
        FROM public.users candidate_user
        WHERE (
              (customer_record.phone IS NOT NULL AND candidate_user.phone = customer_record.phone)
              OR LOWER(candidate_user.email) = LOWER(customer_record.email)
          )
          AND NOT EXISTS (
              SELECT 1
              FROM public.customers existing_customer
              WHERE existing_customer.user_id = candidate_user.id
                AND existing_customer.id <> customer_record.id
          )
        ORDER BY CASE
            WHEN customer_record.phone IS NOT NULL AND candidate_user.phone = customer_record.phone THEN 0
            ELSE 1
        END, candidate_user.id
        LIMIT 1;

        IF linked_user_id IS NULL THEN
            generated_email := customer_record.email;

            IF EXISTS (
                SELECT 1
                FROM public.users
                WHERE LOWER(email) = LOWER(generated_email)
            ) THEN
                generated_email := 'customer-' || customer_record.id || '@servicepilot.local';
            END IF;

            INSERT INTO public.users (
                first_name,
                last_name,
                email,
                password,
                phone,
                active,
                role_id,
                created_at,
                updated_at
            )
            VALUES (
                customer_record.first_name,
                customer_record.last_name,
                generated_email,
                '$2a$10$7mUPn2SGPVHnEMhWQt/m1OVGiH73mN6IBQTw2Bo41iOBwLaH.25RC',
                customer_record.phone,
                TRUE,
                customer_role_id,
                CURRENT_TIMESTAMP,
                NULL
            )
            RETURNING id INTO linked_user_id;
        END IF;

        UPDATE public.customers
        SET user_id = linked_user_id
        WHERE id = customer_record.id;
    END LOOP;

    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uk_customers_user_id'
          AND conrelid = 'public.customers'::regclass
    ) THEN
        ALTER TABLE public.customers
            ADD CONSTRAINT uk_customers_user_id UNIQUE (user_id);
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_customers_user_id'
          AND conrelid = 'public.customers'::regclass
    ) THEN
        ALTER TABLE public.customers
            ADD CONSTRAINT fk_customers_user_id
            FOREIGN KEY (user_id) REFERENCES public.users(id);
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM public.customers
        WHERE user_id IS NULL
    ) THEN
        ALTER TABLE public.customers
            ALTER COLUMN user_id SET NOT NULL;
    END IF;
END $$;

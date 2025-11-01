--
-- PostgreSQL database dump
--

\restrict ovojLQ3TZU8jHdNAnav4W5L4MsYVSuy4zjNSjp9qO0ca9gM1vTea3ynrlBimNBB

-- Dumped from database version 17.6
-- Dumped by pg_dump version 17.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: loan; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.loan (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid NOT NULL,
    requested_amount numeric(15,5) NOT NULL,
    term_in_months integer NOT NULL,
    applied_rate numeric(5,5) NOT NULL,
    request_status character varying(255) NOT NULL,
    loan_status character varying(255) NOT NULL,
    first_due_date date NOT NULL
);


ALTER TABLE public.loan OWNER TO postgres;

--
-- Name: rate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rate (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    min_term integer NOT NULL,
    max_term integer NOT NULL,
    rate_value numeric(5,5) NOT NULL
);


ALTER TABLE public.rate OWNER TO postgres;

--
-- Data for Name: loan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.loan (id, user_id, requested_amount, term_in_months, applied_rate, request_status, loan_status, first_due_date) FROM stdin;
50c3a1cd-55b9-44ef-a08c-f85d7d9e6c06	850a54ca-c302-40ad-b5a9-58beead9ccae	10000.00000	10	0.04999	APPROVED	ACTIVE	2025-11-30
3d6471d7-1242-4404-b87e-68b67d0de6d9	36cbd800-26fe-4058-8331-064e45bada7f	5000.00000	5	0.03999	APPROVED	ACTIVE	2025-11-15
5515c156-7d26-4bb3-8712-ce23309f77f2	a05a352d-c5e4-416d-a77b-212ba993dbea	100000.00000	3	0.03999	APPROVED	COMPLETED	2025-11-10
3abb30e5-9ef1-46ae-ad8a-582d1cc520d0	a05a352d-c5e4-416d-a77b-212ba993dbea	20000.00000	2	0.03999	APPROVED	ACTIVE	2025-11-05
103beb84-e6f2-407d-985d-2ce30e607aa6	3857caf6-5aee-462f-aaa2-90b3b47d20b7	50000.00000	2	0.03999	REJECTED	ACTIVE	2026-01-10
fafcd6b9-8080-4dc5-bc9b-b25a733c1668	3857caf6-5aee-462f-aaa2-90b3b47d20b7	10000.00000	3	0.03999	REJECTED	ACTIVE	2026-01-10
815a7bb6-5bcd-4c63-8f7e-5d55ec8e3fcc	3857caf6-5aee-462f-aaa2-90b3b47d20b7	5000.00000	5	0.03999	APPROVED	ACTIVE	2026-01-10
42fbb3df-6168-4f81-b19e-9188e677483f	27901976-ec42-46f6-bb86-6e604c0d6a2a	5000.00000	5	0.03999	APPROVED	ACTIVE	2026-01-10
b85ebfd5-fd96-49dc-a0a4-a45253423823	43c228cf-f9d6-4cc3-8244-a6f50c6e82ee	500000.00000	10	0.04999	APPROVED	ACTIVE	2025-11-01
5a767164-92a0-4d4b-b9d4-7e4f552a2c85	536b6070-a7f1-47dc-b869-25102892e0bd	3000.00000	3	0.03999	APPROVED	COMPLETED	2025-11-15
d1a784d6-ff55-4e02-bc3e-419d0d168b1f	02a86a8c-e769-4dc8-a036-74fc17918335	5000.00000	3	0.03999	APPROVED	ACTIVE	2025-11-01
\.


--
-- Data for Name: rate; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rate (id, min_term, max_term, rate_value) FROM stdin;
6a1106a9-e598-4a0b-a966-ed1ddb060452	1	6	0.03999
10973f16-a171-4dfb-a9c2-181d81c3bf3b	7	12	0.04999
f919b60e-68fd-483b-81e0-cfe8b6e27938	13	24	0.05999
79d7fc55-570b-465b-8214-5dfbe8b312fe	25	36	0.06999
4a4bdba5-7f98-467b-8f6d-dab68ca0ae21	37	48	0.07999
\.


--
-- Name: loan loan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan
    ADD CONSTRAINT loan_pkey PRIMARY KEY (id);


--
-- Name: rate rate_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rate
    ADD CONSTRAINT rate_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

\unrestrict ovojLQ3TZU8jHdNAnav4W5L4MsYVSuy4zjNSjp9qO0ca9gM1vTea3ynrlBimNBB


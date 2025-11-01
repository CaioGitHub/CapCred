--
-- PostgreSQL database dump
--

\restrict 0oPGA8xkl5WcC8LonEfulsS21k4deHiWoURty7LHXQ3NNwj92P1dxsboL3t8c26

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
-- Name: refresh_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.refresh_token (
    id uuid NOT NULL,
    expiry_date timestamp(6) without time zone NOT NULL,
    token character varying(255) NOT NULL,
    user_id uuid NOT NULL
);


ALTER TABLE public.refresh_token OWNER TO postgres;

--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuarios (
    id uuid NOT NULL,
    cpf character varying(14) NOT NULL,
    email character varying(150) NOT NULL,
    monthly_income numeric(10,2) NOT NULL,
    name character varying(100) NOT NULL,
    password_hash character varying(255) NOT NULL,
    role character varying(20) NOT NULL,
    CONSTRAINT usuarios_role_check CHECK (((role)::text = ANY ((ARRAY['ADMIN'::character varying, 'CLIENT'::character varying])::text[])))
);


ALTER TABLE public.usuarios OWNER TO postgres;

--
-- Data for Name: refresh_token; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.refresh_token (id, expiry_date, token, user_id) FROM stdin;
b72952de-3184-41e4-b221-5b7881ab44da	2025-11-08 01:53:10.998676	c064914a-028d-4a73-bf9c-de29b9ad07b5	94465567-e9f1-4e9a-b6b2-59bbb01a8141
df628c90-80e6-47f0-b2f4-5be27313db83	2025-11-08 01:53:17.51763	167d1ae2-4d03-4434-8057-7887e7db9647	1ad7401e-5df1-4c68-b76d-601104367c80
a4be0f17-a953-4493-b9bc-288114c54cc5	2025-11-08 01:53:24.156433	b5ccfa59-919a-4d91-b859-a9b4dd954192	a4be3ceb-80dd-4c82-ae16-7f81e1fddd9b
4af929c6-efdf-483f-97be-a94e263c6b4e	2025-11-08 01:53:30.599589	219341f9-84dd-4637-9fee-71946689c5e7	34db0f94-3dd9-4894-8721-3e3500291569
f1cbea9c-fd32-41ca-ab2f-47d56d3e814b	2025-11-08 01:53:37.20369	94276bf3-6b94-4785-b806-a2532e98292c	c2e40eb5-a8f5-493c-99cd-0b0f98564c32
148ee8c0-7637-4f9d-8320-7cc9bb365193	2025-11-08 01:53:44.769702	23bc13a9-2de1-4204-945e-9f52c1ca9903	47bb20a2-be42-45fa-aabb-17fc60721b1a
c16f0d33-7f1d-4b09-a5c4-6ad1d3b0ed71	2025-11-08 01:53:51.617257	1e510fae-cdb2-4dfc-b1f3-f3aca152ed7d	88dc33c3-4683-4a9c-8a34-466b48173807
c65aa5fd-81a4-4cb7-b922-38e208a6f013	2025-11-08 01:54:40.242521	3797eec0-dc26-4ac5-827c-06d330cff0b7	25b89271-ff63-4388-bdc5-843ef4acaee5
3d4d2f5e-0fa7-47c6-9bdf-37b3e63b62e2	2025-11-08 01:56:53.52257	09d6fbcf-8d62-4f84-b215-0e9f8075bba4	850a54ca-c302-40ad-b5a9-58beead9ccae
c130e731-4f74-4d0e-9ccf-4aaa9ff9746a	2025-11-08 02:00:14.740798	52ee5bf1-67e1-4eed-9fe8-29f831db3b73	36cbd800-26fe-4058-8331-064e45bada7f
e17856ff-b018-4a6e-99ba-3bce2d843cd6	2025-11-08 02:09:07.67999	90f9f28c-95e6-4cea-afc9-cd4c6854675d	a05a352d-c5e4-416d-a77b-212ba993dbea
ce2b2591-4a09-4814-9051-5358a3be7a64	2025-11-08 02:10:29.316464	c6697c0b-b668-45a6-a915-cd1b5babcdd2	3857caf6-5aee-462f-aaa2-90b3b47d20b7
552abf21-8ff7-40cd-ac8f-70c447496d40	2025-11-08 02:14:02.507817	e7b86846-4305-4b54-bcc3-7d4ca7f0932f	27901976-ec42-46f6-bb86-6e604c0d6a2a
870f4826-c636-44cd-9aa9-d076480b67b5	2025-11-08 02:15:35.613749	c0550a3a-080f-417a-8a91-e70431e56e4b	43c228cf-f9d6-4cc3-8244-a6f50c6e82ee
3144c736-522c-41f0-b473-6628ee3e3762	2025-11-08 02:20:28.239563	45fb134e-fd4f-4531-b897-5d9c30191097	536b6070-a7f1-47dc-b869-25102892e0bd
f4175836-499a-49b0-a007-2b1841974f60	2025-11-08 02:24:52.503526	b9caa3e2-11db-4d36-a072-dee7f5b1159a	02a86a8c-e769-4dc8-a036-74fc17918335
\.


--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuarios (id, cpf, email, monthly_income, name, password_hash, role) FROM stdin;
850a54ca-c302-40ad-b5a9-58beead9ccae	111.111.111-11	peter@email.com	13500.25	Peter Parker	$2a$10$uEgMkCY6zqrIPTYlYanxQuVKPw6mK7wicEdby3RGDGpUuZFpEhwgy	CLIENT
36cbd800-26fe-4058-8331-064e45bada7f	222.222.222-22	diana@email.com	95000.00	Diana Prince	$2a$10$ckwunwPmAMnNivlkZGW.RuABCPhWy5RRDotlZoir8KbSD01Wk2obi	CLIENT
a05a352d-c5e4-416d-a77b-212ba993dbea	333.333.333-33	bruce@email.com	999999.99	Bruce Wayne	$2a$10$bJATDHVkQK1IqYfi6tJMFuKwvsp/BRLC1dagmn2dSjjk/fJWnaoNK	CLIENT
3857caf6-5aee-462f-aaa2-90b3b47d20b7	444.444.444-44	clark@email.com	7200.50	Clark Kent	$2a$10$GvzAPT5BGEUFDTtizCxKUODqcDV8JR7XQwS7dNezzdLkh4.OOUnFy	CLIENT
27901976-ec42-46f6-bb86-6e604c0d6a2a	555.555.555-55	natasha@email.com	45000.10	Natasha Romanoff	$2a$10$3WULZpA/Fc.z3zwsp5U7xeXAX/wfSNRq1Cclob2YVFMGU3gkAhdm6	CLIENT
43c228cf-f9d6-4cc3-8244-a6f50c6e82ee	666.666.666-66	tony@email.com	1500000.00	Tony Stark	$2a$10$JZL/Yhup6kLNJZng1XrSj..Rr1aHjA1gRehLBilC3rPfCi8CnpjJe	CLIENT
536b6070-a7f1-47dc-b869-25102892e0bd	777.777.777-77	steve@email.com	5500.00	Steve Rogers	$2a$10$uMDASBf3TQxxPeDZd7.txOnPEz4fIFydjJuyE1UQQyDYHHocdU7kq	CLIENT
02a86a8c-e769-4dc8-a036-74fc17918335	888.888.888-88	wanda@email.com	8990.75	Wanda Maximoff	$2a$10$e44pLsCKTaPN7VJuPecbyevDDieE0DG6U4x.8n7oyiNd9OTqRnAsi	CLIENT
94465567-e9f1-4e9a-b6b2-59bbb01a8141	999.999.999-99	stephen@email.com	120000.00	Stephen Strange	$2a$10$AY9r2c4oFhwtsilGZrBsru6Y9EvtX2luOcQtSvSezF.EbVU0/qamC	CLIENT
1ad7401e-5df1-4c68-b76d-601104367c80	010.101.010-00	barry@email.com	6100.00	Barry Allen	$2a$10$.Q9zPPGc3VVv43HGzLJga.FnW0r6V2lT8f4hsut4KbVDjEW/8DI6i	CLIENT
a4be3ceb-80dd-4c82-ae16-7f81e1fddd9b	020.202.020-00	hal@email.com	15250.50	Hal Jordan	$2a$10$TLp6yKo0HThQKmLrNhgHs.Lv5vs2AldYnMivsKY6hRRlrgS9hvowG	CLIENT
34db0f94-3dd9-4894-8721-3e3500291569	030.303.030-00	t'challa@email.com	50000.00	T'Challa	$2a$10$/4T/PXtI2XX9N7oegpb4BOht4JV7.VpTa4BXlhUbPzHI.pa4k8PnS	CLIENT
c2e40eb5-a8f5-493c-99cd-0b0f98564c32	040.404.040-00	scott@email.com	4900.00	Scott Lang	$2a$10$tMsMqABKfHPERg9z0uJ7kOp2XlyG1Vkxn6txpq.pjr1qN2PiRXIU2	CLIENT
47bb20a2-be42-45fa-aabb-17fc60721b1a	050.505.050-00	arthur@email.com	18000.00	Arthur Curry	$2a$10$N3UnBGXclGCU/H68NoJmcOvUZTRmRyu.QeGMrNUk4MbgFJ/rP0n0q	CLIENT
88dc33c3-4683-4a9c-8a34-466b48173807	060.606.060-00	carol@email.com	75000.00	Carol Danvers	$2a$10$rKyw15SfQtz/Dy9FjwgypOgJSahdcI.tIp8tu7yTguikoGr/9Y7.m	CLIENT
25b89271-ff63-4388-bdc5-843ef4acaee5	999.888.777-66	admin@email.com	75000.00	admin	$2a$10$2lhf4fCBj6LSXdm5.tlpZO32uxH9kctSXowZGvatwgqEL.BAGv0d.	ADMIN
\.


--
-- Name: refresh_token refresh_token_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refresh_token
    ADD CONSTRAINT refresh_token_pkey PRIMARY KEY (id);


--
-- Name: usuarios uk2et2smpfrtsohr7w9fe1v8a5e; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT uk2et2smpfrtsohr7w9fe1v8a5e UNIQUE (cpf);


--
-- Name: usuarios ukkfsp0s1tflm1cwlj8idhqsad0; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT ukkfsp0s1tflm1cwlj8idhqsad0 UNIQUE (email);


--
-- Name: refresh_token ukr4k4edos30bx9neoq81mdvwph; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refresh_token
    ADD CONSTRAINT ukr4k4edos30bx9neoq81mdvwph UNIQUE (token);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

\unrestrict 0oPGA8xkl5WcC8LonEfulsS21k4deHiWoURty7LHXQ3NNwj92P1dxsboL3t8c26


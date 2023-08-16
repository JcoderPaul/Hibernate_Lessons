CREATE TABLE public.talkers_chats
(
    talker_id BIGINT REFERENCES public.talkers (talker_id),
    chat_id BIGINT REFERENCES public.chats (chat_id),
    PRIMARY KEY (talker_id, chat_id)
);
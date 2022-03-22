package com.sunny.chattingmachine;

import com.sunny.chattingmachine.repository.AccountRepository;
import com.sunny.chattingmachine.repository.CommentRepository;
import com.sunny.chattingmachine.repository.PostRepository;
import org.springframework.stereotype.Component;


@Component
public class InitService {

    @Component
    private static class Init {
        private final AccountRepository accountRepository;
        private final PostRepository postRepository;
        private final CommentRepository commentRepository;

        public Init(AccountRepository accountRepository, PostRepository postRepository, CommentRepository commentRepository) {
            this.accountRepository = accountRepository;
            this.postRepository = postRepository;
            this.commentRepository = commentRepository;
        }

    }

}

/*
 * Copyright (c) 2016, Mihai Emil Andronache
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1)Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *  2)Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  3)Neither the name of charles-github-ejb nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.amihaiemil.charles.github;

import javax.json.JsonObject;

import org.slf4j.Logger;

import com.amihaiemil.charles.steps.Step;

/**
 * Step where the identity of the command author is checked.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $id$
 * @since 1.0.0
 *
 */
public class AuthorOwnerCheck implements Step {

	/**
	 * Command.
	 */
	private Command com;
	
	/**
	 * Json repo as returned by the Github API.
	 */
	private JsonObject repo;
	
	/**
	 * Logger of the action.
	 */
	private Logger logger;

	/**
	 * Constructor.
	 * @param command Command received.
	 * @param repo Json repo object.
	 * @param logger Action logger.
	 */
	public AuthorOwnerCheck(Command command, JsonObject repo, Logger logger) {
		this.com = command;
		this.repo = repo;
		this.logger = logger;
	}

	/**
	 * Check that the author of a command is owner of the repo.
	 * @return true if the check is successful, false otherwise
	 */
	@Override
	public boolean perform() {
        logger.info("Checking ownership of the repo");
        String repoOwner = repo.getJsonObject("owner").getString("login");
        if(repoOwner.equals(com.authorLogin())) {
            logger.info("Commander is repo owner - OK");
            return true;
        }
        logger.warn("The commander needs to be owner of the repo!");
        return false;
    }
	
}

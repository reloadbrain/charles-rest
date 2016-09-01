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
import com.amihaiemil.charles.steps.IndexSite;
import com.amihaiemil.charles.steps.Step;

/**
 * Step taken by the Github agent when receiving an indexsite command. 
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public class IndexSiteSteps implements Step {

	/**
	 * Index site command.
	 */
	private Command com;

	/**
	 * Json repository as returned by the Github API.
	 */
	private JsonObject repoJson;

    /**
     * Preconditions that have to be met in order to preform this step;
     */
    private Step preconditions;

    /**
     * To perform after the index command has been executed successfully.
     */
    private Step followup;

    /**
     * Constructor.
     * @param com Command.
 
     */
    public IndexSiteSteps(Command com, JsonObject repo, Step precStep, Step followup) {
        this.com = com;
        this.repoJson = repo;
        this.preconditions = precStep;
        this.followup = followup;
    }

    /**
     * Perform the steps necessary to fulfill an indexsite command.<br><br>
     * 1) Check the preconditions.
     * 2) Perform he index if the preconditions are met.
     * 3) Perform the followup steps.
     */
    @Override
    public boolean perform() {
        if(this.preconditions.perform()) {
            String expectedName = this.repoJson.getJsonObject("owner").getString("login") + ".github.io";
            boolean indexed = false;
            if(expectedName.equals(this.repoJson.getString("name"))) {
            	indexed = this.indexSiteStep(this.com.authorLogin(), repoJson.getString("name"), false).perform();
            } else {
            	indexed = this.indexSiteStep(this.com.authorLogin(), repoJson.getString("name"), true).perform();
            }
        if(indexed) {
                return followup.perform();
            }
        }
		return false;
	}

    /**
     * Return an IndexSite Step for the given repo.
     * @param ownerLogin Username of the repo's owner.
     * @param repoName Repository name.
     * @param ghPages true if the Repo is not a site repository (owner.github.io) but has a gh-pages branch; false otherwise.
     * @return IndexSite instance.
     */
    IndexSite indexSiteStep(String ownerLogin, String repoName, boolean ghPages) {
    	if(!ghPages) {
    		return new IndexSite("http://" + repoName);
    	}
    	return new IndexSite("http://" + ownerLogin + ".github.io/" + repoName);
    }

}
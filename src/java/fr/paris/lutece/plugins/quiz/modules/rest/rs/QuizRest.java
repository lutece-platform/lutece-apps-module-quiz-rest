/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.quiz.modules.rest.rs;

import fr.paris.lutece.plugins.quiz.service.QuizService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.rest.util.json.JSONUtil;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


/**
 * QuizRest
 */
@Path( RestConstants.BASE_PATH + "quiz" )
public class QuizRest
{
    private static final String BEAN_QUIZ_SERVICE = "quiz.quizService";

    /**
     * Return quiz list in json format
     * @return The JSON quiz list
     */
    @GET
    @Path( "" )
    @Produces( {MediaType.TEXT_PLAIN,
        MediaType.APPLICATION_JSON
    } )
    public String getQuizListJson(  )
    {
        QuizService serviceQuiz = (QuizService) SpringContextService.getBean( BEAN_QUIZ_SERVICE );

        return JSONUtil.model2Json( serviceQuiz.getQuizList(  ) );
    }

    /**
     * Return Quiz data
     * @param strId The Quiz ID
     * @return JSON data of the quiz
     */
    @GET
    @Path( "{id}" )
    @Produces( {MediaType.TEXT_PLAIN,
        MediaType.APPLICATION_JSON
    } )
    public String getQuizJson( @PathParam( "id" )
    String strId, @Context
    HttpServletResponse response )
    {
        String strResponse = "";

        try
        {
            strResponse = JSONUtil.model2Json( getQuiz( strId ) );
        }
        catch ( NumberFormatException e )
        {
            response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
            strResponse = JSONUtil.formatError( "Invalid quiz id", 1 );
        }
        catch ( NullPointerException e )
        {
            response.setStatus( HttpServletResponse.SC_NOT_FOUND );
            strResponse = JSONUtil.formatError( "Quiz id not found", 1 );
        }

        return strResponse;
    }

    private Map getQuiz( String strId )
    {
        QuizService serviceQuiz = (QuizService) SpringContextService.getBean( BEAN_QUIZ_SERVICE );

        return serviceQuiz.getQuiz( Integer.parseInt( strId ) );
    }
}

////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2003  Oliver Burn
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////
package com.puppycrawl.tools.checkstyle.checks.usage;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.ScopeUtils;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;


/**
 * <p>Checks that a parameter is used.
 * </p>
 * <p>
 * An example of how to configure the check is:
 * </p>
 * <pre>
 * &lt;module name="usage.UnusedParameter"/&gt;
 * </pre>
 * <p>
 * @author Rick Giles
 */
public class UnusedParameterCheck extends AbstractUsageCheck
{
    /** controls checking of catch clause parameter */
    private boolean mIgnoreCatch = true;
    
    /** @see com.puppycrawl.tools.checkstyle.api.Check */
    public int[] getDefaultTokens()
    {
        return new int[] {
            TokenTypes.PARAMETER_DEF,
        };
    }
    
    /** @see com.puppycrawl.tools.checkstyle.checks.usage.AbstractUsageCheck */
    public String getErrorKey()
    {
        return "unused.parameter";
    }

    /** @see com.puppycrawl.tools.checkstyle.checks.usage.AbstractUsageCheck */
    public boolean mustCheckReferenceCount(DetailAST aAST)
    {
        boolean result = false;
        final DetailAST parent = aAST.getParent();
        if (parent != null) {
            if (parent.getType() == TokenTypes.PARAMETERS) {
                final DetailAST grandparent = parent.getParent();
                if (grandparent != null) {
                    if (grandparent.getType() == TokenTypes.METHOD_DEF)
                    {
                        final DetailAST modifiersAST =
                        grandparent.findFirstToken(TokenTypes.MODIFIERS);
                        if ((modifiersAST != null)
                            && !modifiersAST.branchContains(TokenTypes.ABSTRACT)
                            && !ScopeUtils.inInterfaceBlock(aAST))
                        {
                            result = true;
                        }                   
                    }
                    else if (grandparent.getType() == TokenTypes.CTOR_DEF) {
                        result = true;
                    }               
                }
            }
            else if (parent.getType() == TokenTypes.LITERAL_CATCH) {
                result = !mIgnoreCatch;
            }
        }
        return result;   
    }
    
    /**
     * Control whether unused catch clause parameters are flagged.
     * @param aIgnoreCatch whether unused catch clause parameters
     * should be flagged.
     */
    public void setIgnoreCatch(boolean aIgnoreCatch)
    {
        mIgnoreCatch = aIgnoreCatch;
    }

}
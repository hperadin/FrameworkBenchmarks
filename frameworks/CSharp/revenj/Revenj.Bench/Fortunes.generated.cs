﻿#pragma warning disable 1591
//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.34209
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace ASP
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    
    #line 2 "..\..\Fortunes.cshtml"
    using System.Web;
    
    #line default
    #line hidden
    
    [System.CodeDom.Compiler.GeneratedCodeAttribute("RazorGenerator", "2.0.0.0")]
    public partial class _Fortunes_cshtml : RazorGenerator.Templating.RazorTemplateBase
    {
#line hidden

        #line 3 "..\..\Fortunes.cshtml"

    public List<HelloWorld.Fortune> Model { get; set; }

        #line default
        #line hidden

        public override void Execute()
        {










WriteLiteral("<!DOCTYPE html><html><head><title>Fortunes</title></head><body><table><tr><th>id<" +
"/th><th>message</th></tr>");




            
            #line 7 "..\..\Fortunes.cshtml"
   foreach (var it in Model)
{

            
            #line default
            #line hidden
WriteLiteral("<tr><td>");


            
            #line 9 "..\..\Fortunes.cshtml"
   Write(it.id);

            
            #line default
            #line hidden
WriteLiteral("</td><td>");


            
            #line 9 "..\..\Fortunes.cshtml"
                  Write(HttpUtility.HtmlEncode(it.message));

            
            #line default
            #line hidden
WriteLiteral("</td></tr>");


            
            #line 9 "..\..\Fortunes.cshtml"
                                                                    
            
            #line default
            #line hidden

            
            #line 10 "..\..\Fortunes.cshtml"
  }

            
            #line default
            #line hidden
WriteLiteral("</table></body></html>");


        }
    }
}
#pragma warning restore 1591

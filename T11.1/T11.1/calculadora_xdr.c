/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include "calculadora.h"

bool_t
xdr_variables (XDR *xdrs, variables *objp)
{
	register int32_t *buf;

	 if (!xdr_float (xdrs, &objp->a))
		 return FALSE;
	 if (!xdr_float (xdrs, &objp->b))
		 return FALSE;
	 if (!xdr_char (xdrs, &objp->op))
		 return FALSE;
	return TRUE;
}

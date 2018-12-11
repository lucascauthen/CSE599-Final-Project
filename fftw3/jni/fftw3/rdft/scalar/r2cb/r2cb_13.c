/*
 * Copyright (c) 2003, 2007-14 Matteo Frigo
 * Copyright (c) 2003, 2007-14 Massachusetts Institute of Technology
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */

/* This file was automatically generated --- DO NOT EDIT */
/* Generated on Sat Jul 30 16:49:27 EDT 2016 */

#include "codelet-rdft.h"

#ifdef HAVE_FMA

/* Generated by: ../../../genfft/gen_r2cb.native -fma -reorder-insns -schedule-for-pipeline -compact -variables 4 -pipeline-latency 4 -sign 1 -n 13 -name r2cb_13 -include r2cb.h */

/*
 * This function contains 76 FP additions, 58 FP multiplications,
 * (or, 18 additions, 0 multiplications, 58 fused multiply/add),
 * 76 stack variables, 26 constants, and 26 memory accesses
 */
#include "r2cb.h"

static void r2cb_13(R *R0, R *R1, R *Cr, R *Ci, stride rs, stride csr, stride csi, INT v, INT ivs, INT ovs)
{
     DK(KP968287244, +0.968287244361984016049539446938120421179794516);
     DK(KP875502302, +0.875502302409147941146295545768755143177842006);
     DK(KP1_150281458, +1.150281458948006242736771094910906776922003215);
     DK(KP1_040057143, +1.040057143777729238234261000998465604986476278);
     DK(KP1_200954543, +1.200954543865330565851538506669526018704025697);
     DK(KP769338817, +0.769338817572980603471413688209101117038278899);
     DK(KP600925212, +0.600925212577331548853203544578415991041882762);
     DK(KP1_033041561, +1.033041561246979445681802577138034271410067244);
     DK(KP1_007074065, +1.007074065727533254493747707736933954186697125);
     DK(KP503537032, +0.503537032863766627246873853868466977093348562);
     DK(KP581704778, +0.581704778510515730456870384989698884939833902);
     DK(KP859542535, +0.859542535098774820163672132761689612766401925);
     DK(KP166666666, +0.166666666666666666666666666666666666666666667);
     DK(KP2_000000000, +2.000000000000000000000000000000000000000000000);
     DK(KP301479260, +0.301479260047709873958013540496673347309208464);
     DK(KP226109445, +0.226109445035782405468510155372505010481906348);
     DK(KP686558370, +0.686558370781754340655719594850823015421401653);
     DK(KP514918778, +0.514918778086315755491789696138117261566051239);
     DK(KP957805992, +0.957805992594665126462521754605754580515587217);
     DK(KP522026385, +0.522026385161275033714027226654165028300441940);
     DK(KP853480001, +0.853480001859823990758994934970528322872359049);
     DK(KP038632954, +0.038632954644348171955506895830342264440241080);
     DK(KP612264650, +0.612264650376756543746494474777125408779395514);
     DK(KP302775637, +0.302775637731994646559610633735247973125648287);
     DK(KP866025403, +0.866025403784438646763723170752936183471402627);
     DK(KP500000000, +0.500000000000000000000000000000000000000000000);
     {
	  INT i;
	  for (i = v; i > 0; i = i - 1, R0 = R0 + ovs, R1 = R1 + ovs, Cr = Cr + ivs, Ci = Ci + ivs, MAKE_VOLATILE_STRIDE(52, rs), MAKE_VOLATILE_STRIDE(52, csr), MAKE_VOLATILE_STRIDE(52, csi)) {
	       E TW, T14, TS, TO, T18, T1e, TY, TX, TQ, Tq, TP, Tl, T1d, Tr;
	       {
		    E T1, TN, T16, TJ, TV, TG, TU, Tf, T2, T3, Tb, Ti, T4;
		    {
			 E Ts, TB, Tx, Ty, Tv, TE, Tt, Tu, Tz, TC;
			 Ts = Ci[WS(csi, 5)];
			 Tt = Ci[WS(csi, 2)];
			 Tu = Ci[WS(csi, 6)];
			 TB = Ci[WS(csi, 1)];
			 Tx = Ci[WS(csi, 3)];
			 Ty = Ci[WS(csi, 4)];
			 Tv = Tt + Tu;
			 TE = Tu - Tt;
			 T1 = Cr[0];
			 Tz = Tx + Ty;
			 TC = Tx - Ty;
			 {
			      E TL, Tw, T7, Ta;
			      TL = Ts + Tv;
			      Tw = FNMS(KP500000000, Tv, Ts);
			      T7 = Cr[WS(csr, 5)];
			      {
				   E TD, TM, TA, TH;
				   TD = FNMS(KP500000000, TC, TB);
				   TM = TB + TC;
				   TA = FMA(KP866025403, Tz, Tw);
				   TH = FNMS(KP866025403, Tz, Tw);
				   TN = FMA(KP302775637, TM, TL);
				   T16 = FNMS(KP302775637, TL, TM);
				   {
					E TF, TI, T8, T9;
					TF = FMA(KP866025403, TE, TD);
					TI = FNMS(KP866025403, TE, TD);
					T8 = Cr[WS(csr, 2)];
					T9 = Cr[WS(csr, 6)];
					TJ = FNMS(KP612264650, TI, TH);
					TV = FMA(KP612264650, TH, TI);
					TG = FNMS(KP038632954, TF, TA);
					TU = FMA(KP038632954, TA, TF);
					Tf = T8 - T9;
					Ta = T8 + T9;
				   }
			      }
			      T2 = Cr[WS(csr, 1)];
			      T3 = Cr[WS(csr, 3)];
			      Tb = T7 + Ta;
			      Ti = FMS(KP500000000, Ta, T7);
			      T4 = Cr[WS(csr, 4)];
			 }
		    }
		    {
			 E T17, TK, T5, Te, Tk, Td;
			 TW = FMA(KP853480001, TV, TU);
			 T17 = FNMS(KP853480001, TV, TU);
			 TK = FNMS(KP853480001, TJ, TG);
			 T14 = FMA(KP853480001, TJ, TG);
			 T5 = T3 + T4;
			 Te = T3 - T4;
			 {
			      E Tn, Tg, Th, T6;
			      TS = FNMS(KP522026385, TK, TN);
			      TO = FMA(KP957805992, TN, TK);
			      Tn = Te - Tf;
			      Tg = Te + Tf;
			      Th = FNMS(KP500000000, T5, T2);
			      T6 = T2 + T5;
			      T18 = FNMS(KP522026385, T17, T16);
			      T1e = FMA(KP957805992, T16, T17);
			      {
				   E Tm, Tj, Tc, Tp, To;
				   Tm = Th + Ti;
				   Tj = Th - Ti;
				   Tc = T6 + Tb;
				   Tp = T6 - Tb;
				   To = FNMS(KP514918778, Tn, Tm);
				   TY = FMA(KP686558370, Tm, Tn);
				   TX = FNMS(KP226109445, Tg, Tj);
				   Tk = FMA(KP301479260, Tj, Tg);
				   R0[0] = FMA(KP2_000000000, Tc, T1);
				   Td = FNMS(KP166666666, Tc, T1);
				   TQ = FNMS(KP859542535, To, Tp);
				   Tq = FMA(KP581704778, Tp, To);
			      }
			 }
			 TP = FNMS(KP503537032, Tk, Td);
			 Tl = FMA(KP1_007074065, Tk, Td);
		    }
	       }
	       T1d = FNMS(KP1_033041561, Tq, Tl);
	       Tr = FMA(KP1_033041561, Tq, Tl);
	       {
		    E T13, TR, T19, TZ;
		    T13 = FNMS(KP600925212, TQ, TP);
		    TR = FMA(KP600925212, TQ, TP);
		    T19 = FMA(KP769338817, TY, TX);
		    TZ = FNMS(KP769338817, TY, TX);
		    R0[WS(rs, 4)] = FMA(KP1_200954543, T1e, T1d);
		    R1[WS(rs, 2)] = FNMS(KP1_200954543, T1e, T1d);
		    R0[WS(rs, 6)] = FMA(KP1_200954543, TO, Tr);
		    R1[0] = FNMS(KP1_200954543, TO, Tr);
		    {
			 E T1b, T15, T11, TT;
			 T1b = FNMS(KP1_040057143, T14, T13);
			 T15 = FMA(KP1_040057143, T14, T13);
			 T11 = FMA(KP1_150281458, TS, TR);
			 TT = FNMS(KP1_150281458, TS, TR);
			 {
			      E T1c, T1a, T12, T10;
			      T1c = FMA(KP875502302, T19, T18);
			      T1a = FNMS(KP875502302, T19, T18);
			      T12 = FMA(KP968287244, TZ, TW);
			      T10 = FNMS(KP968287244, TZ, TW);
			      R1[WS(rs, 5)] = FMA(KP1_150281458, T1c, T1b);
			      R0[WS(rs, 3)] = FNMS(KP1_150281458, T1c, T1b);
			      R1[WS(rs, 3)] = FMA(KP1_150281458, T1a, T15);
			      R0[WS(rs, 1)] = FNMS(KP1_150281458, T1a, T15);
			      R0[WS(rs, 5)] = FMA(KP1_040057143, T12, T11);
			      R0[WS(rs, 2)] = FNMS(KP1_040057143, T12, T11);
			      R1[WS(rs, 4)] = FMA(KP1_040057143, T10, TT);
			      R1[WS(rs, 1)] = FNMS(KP1_040057143, T10, TT);
			 }
		    }
	       }
	  }
     }
}

static const kr2c_desc desc = { 13, "r2cb_13", {18, 0, 58, 0}, &GENUS };

void X(codelet_r2cb_13) (planner *p) {
     X(kr2c_register) (p, r2cb_13, &desc);
}

#else				/* HAVE_FMA */

/* Generated by: ../../../genfft/gen_r2cb.native -compact -variables 4 -pipeline-latency 4 -sign 1 -n 13 -name r2cb_13 -include r2cb.h */

/*
 * This function contains 76 FP additions, 35 FP multiplications,
 * (or, 56 additions, 15 multiplications, 20 fused multiply/add),
 * 56 stack variables, 19 constants, and 26 memory accesses
 */
#include "r2cb.h"

static void r2cb_13(R *R0, R *R1, R *Cr, R *Ci, stride rs, stride csr, stride csi, INT v, INT ivs, INT ovs)
{
     DK(KP1_007074065, +1.007074065727533254493747707736933954186697125);
     DK(KP227708958, +0.227708958111581597949308691735310621069285120);
     DK(KP531932498, +0.531932498429674575175042127684371897596660533);
     DK(KP774781170, +0.774781170935234584261351932853525703557550433);
     DK(KP265966249, +0.265966249214837287587521063842185948798330267);
     DK(KP516520780, +0.516520780623489722840901288569017135705033622);
     DK(KP151805972, +0.151805972074387731966205794490207080712856746);
     DK(KP503537032, +0.503537032863766627246873853868466977093348562);
     DK(KP166666666, +0.166666666666666666666666666666666666666666667);
     DK(KP600925212, +0.600925212577331548853203544578415991041882762);
     DK(KP500000000, +0.500000000000000000000000000000000000000000000);
     DK(KP256247671, +0.256247671582936600958684654061725059144125175);
     DK(KP156891391, +0.156891391051584611046832726756003269660212636);
     DK(KP348277202, +0.348277202304271810011321589858529485233929352);
     DK(KP1_150281458, +1.150281458948006242736771094910906776922003215);
     DK(KP300238635, +0.300238635966332641462884626667381504676006424);
     DK(KP011599105, +0.011599105605768290721655456654083252189827041);
     DK(KP1_732050807, +1.732050807568877293527446341505872366942805254);
     DK(KP2_000000000, +2.000000000000000000000000000000000000000000000);
     {
	  INT i;
	  for (i = v; i > 0; i = i - 1, R0 = R0 + ovs, R1 = R1 + ovs, Cr = Cr + ivs, Ci = Ci + ivs, MAKE_VOLATILE_STRIDE(52, rs), MAKE_VOLATILE_STRIDE(52, csr), MAKE_VOLATILE_STRIDE(52, csi)) {
	       E TG, TS, TR, T15, TJ, TT, T1, Tm, Tc, Td, Tg, Tj, Tk, Tn, To;
	       E Tp;
	       {
		    E Ts, Tv, Tw, TE, TC, TB, Tz, TD, TA, TF;
		    {
			 E Tt, Tu, Tx, Ty;
			 Ts = Ci[WS(csi, 1)];
			 Tt = Ci[WS(csi, 3)];
			 Tu = Ci[WS(csi, 4)];
			 Tv = Tt - Tu;
			 Tw = FMS(KP2_000000000, Ts, Tv);
			 TE = KP1_732050807 * (Tt + Tu);
			 TC = Ci[WS(csi, 5)];
			 Tx = Ci[WS(csi, 6)];
			 Ty = Ci[WS(csi, 2)];
			 TB = Tx + Ty;
			 Tz = KP1_732050807 * (Tx - Ty);
			 TD = FNMS(KP2_000000000, TC, TB);
		    }
		    TA = Tw + Tz;
		    TF = TD - TE;
		    TG = FMA(KP011599105, TA, KP300238635 * TF);
		    TS = FNMS(KP011599105, TF, KP300238635 * TA);
		    {
			 E TP, TQ, TH, TI;
			 TP = Ts + Tv;
			 TQ = TB + TC;
			 TR = FNMS(KP348277202, TQ, KP1_150281458 * TP);
			 T15 = FMA(KP348277202, TP, KP1_150281458 * TQ);
			 TH = Tw - Tz;
			 TI = TE + TD;
			 TJ = FMA(KP156891391, TH, KP256247671 * TI);
			 TT = FNMS(KP256247671, TH, KP156891391 * TI);
		    }
	       }
	       {
		    E Tb, Ti, Tf, T6, Th, Te;
		    T1 = Cr[0];
		    {
			 E T7, T8, T9, Ta;
			 T7 = Cr[WS(csr, 5)];
			 T8 = Cr[WS(csr, 2)];
			 T9 = Cr[WS(csr, 6)];
			 Ta = T8 + T9;
			 Tb = T7 + Ta;
			 Ti = FNMS(KP500000000, Ta, T7);
			 Tf = T8 - T9;
		    }
		    {
			 E T2, T3, T4, T5;
			 T2 = Cr[WS(csr, 1)];
			 T3 = Cr[WS(csr, 3)];
			 T4 = Cr[WS(csr, 4)];
			 T5 = T3 + T4;
			 T6 = T2 + T5;
			 Th = FNMS(KP500000000, T5, T2);
			 Te = T3 - T4;
		    }
		    Tm = KP600925212 * (T6 - Tb);
		    Tc = T6 + Tb;
		    Td = FNMS(KP166666666, Tc, T1);
		    Tg = Te + Tf;
		    Tj = Th + Ti;
		    Tk = FMA(KP503537032, Tg, KP151805972 * Tj);
		    Tn = Th - Ti;
		    To = Te - Tf;
		    Tp = FNMS(KP265966249, To, KP516520780 * Tn);
	       }
	       R0[0] = FMA(KP2_000000000, Tc, T1);
	       {
		    E TK, T1b, TV, T12, T16, T18, TO, T1a, Tr, T17, T11, T13;
		    {
			 E TU, T14, TM, TN;
			 TK = KP1_732050807 * (TG + TJ);
			 T1b = KP1_732050807 * (TS - TT);
			 TU = TS + TT;
			 TV = TR - TU;
			 T12 = FMA(KP2_000000000, TU, TR);
			 T14 = TG - TJ;
			 T16 = FMS(KP2_000000000, T14, T15);
			 T18 = T14 + T15;
			 TM = FMA(KP774781170, To, KP531932498 * Tn);
			 TN = FNMS(KP1_007074065, Tj, KP227708958 * Tg);
			 TO = TM - TN;
			 T1a = TM + TN;
			 {
			      E Tl, Tq, TZ, T10;
			      Tl = Td - Tk;
			      Tq = Tm - Tp;
			      Tr = Tl - Tq;
			      T17 = Tq + Tl;
			      TZ = FMA(KP2_000000000, Tk, Td);
			      T10 = FMA(KP2_000000000, Tp, Tm);
			      T11 = TZ - T10;
			      T13 = T10 + TZ;
			 }
		    }
		    R1[WS(rs, 2)] = T11 - T12;
		    R0[WS(rs, 6)] = T13 - T16;
		    R1[0] = T13 + T16;
		    R0[WS(rs, 4)] = T11 + T12;
		    {
			 E TL, TW, T19, T1c;
			 TL = Tr - TK;
			 TW = TO - TV;
			 R1[WS(rs, 3)] = TL - TW;
			 R0[WS(rs, 1)] = TL + TW;
			 T19 = T17 - T18;
			 T1c = T1a + T1b;
			 R1[WS(rs, 1)] = T19 - T1c;
			 R1[WS(rs, 4)] = T1c + T19;
		    }
		    {
			 E T1d, T1e, TX, TY;
			 T1d = T1a - T1b;
			 T1e = T17 + T18;
			 R0[WS(rs, 2)] = T1d + T1e;
			 R0[WS(rs, 5)] = T1e - T1d;
			 TX = Tr + TK;
			 TY = TO + TV;
			 R0[WS(rs, 3)] = TX - TY;
			 R1[WS(rs, 5)] = TX + TY;
		    }
	       }
	  }
     }
}

static const kr2c_desc desc = { 13, "r2cb_13", {56, 15, 20, 0}, &GENUS };

void X(codelet_r2cb_13) (planner *p) {
     X(kr2c_register) (p, r2cb_13, &desc);
}

#endif				/* HAVE_FMA */
